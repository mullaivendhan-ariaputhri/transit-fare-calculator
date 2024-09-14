package com.littlepay.fare.service;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TripRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.littlepay.fare.constants.Constants.CHARGE_AMOUNT;

@Slf4j
@Service
public class TripService {

  @Autowired AppConfig appConfig;

  /**
   * Read taps csv and create a list of Tap Records
   *
   * @return
   */
  public void writeTrips(List<TripRecord> tripRecords) {
    log.debug("Writing Trip records post processing. # of trips - {}", tripRecords.size());
    final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(appConfig.getDateFormat());
    log.info("Date format to write trip records - {}", appConfig.getDateFormat());

    try (Writer writer = new FileWriter(appConfig.getTripsFilePath());
        CSVPrinter csvPrinter =
            new CSVPrinter(
                writer,
                CSVFormat.Builder.create().setHeader(appConfig.getTripsHeaders()).build())) {
      for (TripRecord trip : tripRecords) {
        csvPrinter.printRecord(
            dateFormat.format(trip.getStarted()),
            dateFormat.format(trip.getFinished()),
            trip.getDurationSecs(),
            trip.getFromStopId(),
            trip.getToStopId(),
            String.format(CHARGE_AMOUNT, trip.getChargeAmount()),
            trip.getCompanyId(),
            trip.getBusID(),
            trip.getPan(),
            trip.getStatus());
      }
      csvPrinter.flush();
    } catch (IOException e) {
      log.error(
          "Exception occurred in writing CSV file - {} with message - {}",
          appConfig.getTripsFilePath(),
          e.getMessage());
    }
  }
}
