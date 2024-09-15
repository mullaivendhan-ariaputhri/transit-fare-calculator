package com.littlepay.fare.service;

import static com.littlepay.fare.constants.Constants.*;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TapRecord;
import com.littlepay.fare.exception.FareCalculatorException;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TapService {

  @Autowired AppConfig appConfig;

  /**
   * Loading Tap details
   *
   * @return List of taps
   */
  public List<TapRecord> getTapRecords() throws FareCalculatorException {
    log.debug("Reading Tap records based on file configuration");
    final DateTimeFormatter dateFormat =
        DateTimeFormatter.ofPattern(appConfig.getDateFormat()).withZone(ZoneOffset.UTC);
    log.info("Date format to parse tap records - {}", appConfig.getDateFormat());
    if (Files.notExists(Path.of(appConfig.getTapsFilePath()))) {
      log.error("Input file {} not found", appConfig.getTapsFilePath());
      throw new FareCalculatorException("Taps file not found");
    }

    List<TapRecord> taps = new ArrayList<>();
    try (Reader reader = new FileReader(appConfig.getTapsFilePath());
        CSVParser csvParser =
            new CSVParser(
                reader,
                CSVFormat.Builder.create()
                    .setHeader(appConfig.getTapsHeaders())
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build())) {
      csvParser.stream()
          .forEach(
              csvRecord ->
                  taps.add(
                      TapRecord.builder()
                          .id(Integer.parseInt(csvRecord.get(ID)))
                          .dateTimeUTC(
                              OffsetDateTime.parse(csvRecord.get(DATE_TIME_UTC), dateFormat))
                          .tapType(csvRecord.get(TAP_TYPE))
                          .stopId(csvRecord.get(STOP_ID))
                          .companyId(csvRecord.get(COMPANY_ID))
                          .busId(csvRecord.get(BUS_ID))
                          .pan(csvRecord.get(PAN))
                          .build()));
    } catch (Exception e) {
      log.error(
          "Exception occurred in reading CSV file - {} with message - {}",
          appConfig.getTapsFilePath(),
          e.getMessage());
      throw new FareCalculatorException("Exception occurred in reading CSV file", e);
    } finally {
      log.info("Successfully loaded Taps info. # - {}", taps.size());
    }

    return taps;
  }
}
