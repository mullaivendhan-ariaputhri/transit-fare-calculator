package com.littlepay.fare.service;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TapRecord;
import com.littlepay.fare.dto.TripRecord;
import com.littlepay.fare.manager.TripFareManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.littlepay.fare.constants.Constants.*;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class TripService {

  @Autowired AppConfig appConfig;

  @Autowired private TripFareManager tripFareManager;

  /**
   * Generate Trips from Taps. Process taps in Groups of PAN and Bus ID. Invoke processGroupedTaps
   * for each group to convert taps to Trips
   *
   * @param taps
   * @return
   */
  public List<TripRecord> generateTripsFromTaps(List<TapRecord> taps) {
    return taps.stream()
        .collect(Collectors.groupingBy(t -> Arrays.asList(t.getPan(), t.getBusId())))
        .values()
        .stream()
        .flatMap(this::processGroupedTaps)
        .collect(Collectors.toList());
  }

  /**
   * Process grouped trips Sort the taps in the order of time, and create trips by evaluating a
   * start/end trip availability
   *
   * @param tapRecords
   * @return
   */
  private Stream<TripRecord> processGroupedTaps(List<TapRecord> tapRecords) {
    List<TapRecord> sortedTaps =
        tapRecords.stream()
            .sorted(Comparator.comparing(TapRecord::getDateTimeUTC))
            .collect(Collectors.toList());
    // Trips generated from grouped taps
    List<TripRecord> trips = new ArrayList<>();

    // Stack to store TAP_ON details until TAP_OFF is found to process.
    Stack<TapRecord> tapOnStack = new Stack<>();

    // Iterate sorted taps and generate Trip
    for (TapRecord tapRecord : sortedTaps) {
      if (TAP_ON.equals(tapRecord.getTapType())) {
        tapOnStack.push(tapRecord);
      } else if (TAP_OFF.equals(tapRecord.getTapType())) {
        if (!tapOnStack.isEmpty()) {
          TapRecord tapOn = tapOnStack.pop();
          TripRecord trip = createTripRecord(tapOn, tapRecord);
          trips.add(trip);
        } else {
          // Assumption : Impossible unless an error in system
          log.error("Tap Off record found without a Tap On, Invalid Record = {}", tapRecord);
        }
      }
    }

    // Incomplete Trips - Orphan TAP ON
    while (!tapOnStack.isEmpty()) {
      trips.add(createTripRecord(tapOnStack.pop(), null));
    }

    return trips.stream();
  }

  /**
   * Util method that creates Trip details based on Taps (On and Off)
   *
   * @param tapOn
   * @param tapOff
   * @return
   */
  private TripRecord createTripRecord(TapRecord tapOn, TapRecord tapOff) {
    TripRecord trip = new TripRecord();
    trip.setStarted(tapOn.getDateTimeUTC());
    trip.setFromStopId(tapOn.getStopId());
    trip.setCompanyId(tapOn.getCompanyId());
    trip.setBusID(tapOn.getBusId());
    trip.setPan(tapOn.getPan());

    if (!isNull(tapOff)) { // Completed Or Cancelled Trips
      trip.setFinished(tapOff.getDateTimeUTC());
      trip.setDurationSecs(
          (int) (tapOff.getDateTimeUTC().toEpochSecond() - tapOn.getDateTimeUTC().toEpochSecond()));
      trip.setToStopId(tapOff.getStopId());
      trip.setChargeAmount(tripFareManager.getFare(tapOn.getStopId(), tapOff.getStopId()));
      if (tapOn.getStopId().equalsIgnoreCase(tapOff.getStopId())) {
        trip.setStatus(CANCELLED);
      } else {
        trip.setStatus(COMPLETED);
      }
    } else { // Incomplete Trips
      trip.setFinished(null);
      trip.setDurationSecs(null);
      trip.setToStopId(null);
      trip.setChargeAmount(tripFareManager.getMaxFare(tapOn.getStopId()));
      trip.setStatus(INCOMPLETE);
    }
    return trip;
  }

  /** Read taps csv and create a list of Tap Records */
  public boolean writeTrips(List<TripRecord> trips) {
    boolean success = false;
    log.debug("Writing Trip records post processing. # of trips - {}", trips.size());
    final DateTimeFormatter dateFormat =
        DateTimeFormatter.ofPattern(appConfig.getDateFormat()).withZone(ZoneOffset.UTC);
    log.info("Date format to write trip records - {}", appConfig.getDateFormat());
    Path filePath = Paths.get(appConfig.getTripsFilePath());
    try (Writer writer = new FileWriter(appConfig.getTripsFilePath());
        CSVPrinter csvPrinter =
            new CSVPrinter(
                writer,
                CSVFormat.Builder.create().setHeader(appConfig.getTripsHeaders()).build())) {
      for (TripRecord trip : trips) {
        csvPrinter.printRecord(
            Optional.ofNullable(trip.getStarted()).map(dateFormat::format).orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getFinished()).map(dateFormat::format).orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getDurationSecs())
                .map(d -> String.format(DURATION_SECS, d))
                .orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getFromStopId()).orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getToStopId()).orElse(NOT_AVAILABLE),
            String.format(CHARGE_AMOUNT, trip.getChargeAmount()),
            Optional.ofNullable(trip.getCompanyId()).orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getBusID()).orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getPan()).orElse(NOT_AVAILABLE),
            Optional.ofNullable(trip.getStatus()).orElse(NOT_AVAILABLE));
      }
      csvPrinter.flush();
    } catch (IOException e) {
      log.error(
          "Exception occurred in writing CSV file - {} with message - {}",
          appConfig.getTripsFilePath(),
          e.getMessage());
    } finally {
      log.info(
          "Trips added to file - {}. # of Trips - {}", appConfig.getTripsFilePath(), trips.size());
      // Check if the file was written successfully
      if (Files.exists(filePath)) {
        try {
          long fileSize = Files.size(filePath);
          log.info("File successfully created. File size: {} bytes", fileSize);
          success = fileSize > 0;
        } catch (IOException e) {
          log.error("Error occurred while checking file size: {}", e.getMessage());
        }
      } else {
        log.error("File not created or does not exist: {}", appConfig.getTripsFilePath());
      }
    }
    return success;
  }
}
