package com.littlepay.fare.service;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TapRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.littlepay.fare.constants.Constants.*;

@Slf4j
@Service
public class TapService {

  @Autowired AppConfig appConfig;

  /**
   * Write Trips as csv from the list of Trip Records
   * @return
   */
  public List<TapRecord> getTapRecords() {
    log.debug("Reading Tap records based on configuration");
    final DateTimeFormatter dateFormat =
        DateTimeFormatter.ofPattern(appConfig.getDateFormat());
    log.info("Date format to parse tap records - {}", appConfig.getDateFormat());

    List<TapRecord> records = new ArrayList<>();
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
                  records.add(
                      TapRecord.builder()
                          .id(Integer.parseInt(csvRecord.get(ID)))
                          .dateTimeUTC(
                              OffsetDateTime.parse(
                                  csvRecord.get(DATE_TIME_UTC),
                                      dateFormat.withZone(ZoneOffset.UTC)))
                          .tapType(csvRecord.get(TAP_TYPE))
                          .stopId(csvRecord.get(STOP_ID))
                          .companyId(csvRecord.get(COMPANY_ID))
                          .busId(csvRecord.get(BUS_ID))
                          .pan(csvRecord.get(PAN))
                          .build()));
    } catch (IOException e) {
      log.error(
          "Exception occurred in reading CSV file - {} with message - {}",
          appConfig.getTapsFilePath(),
          e.getMessage());
    }
    return records;
  }
}
