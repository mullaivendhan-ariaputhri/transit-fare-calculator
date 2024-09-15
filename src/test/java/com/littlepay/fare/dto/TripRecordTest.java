package com.littlepay.fare.dto;

import static com.littlepay.fare.constants.Constants.TRIP_RECORD;

import java.time.OffsetDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TripRecordTest {

  @Test
  void testSettersAndGetters() {
    TripRecord tripRecord = new TripRecord();
    OffsetDateTime now = OffsetDateTime.now();
    tripRecord.setStarted(now);
    tripRecord.setFinished(now);
    tripRecord.setDurationSecs(3600);
    tripRecord.setFromStopId("Stop1");
    tripRecord.setToStopId("Stop2");
    tripRecord.setChargeAmount(2.50);
    tripRecord.setCompanyId("Company1");
    tripRecord.setBusID("Bus37");
    tripRecord.setPan("5500005555555559");
    tripRecord.setStatus("COMPLETED");

    Assertions.assertThat(tripRecord.getStarted()).isEqualTo(now);
    Assertions.assertThat(tripRecord.getFinished()).isEqualTo(now);
    Assertions.assertThat(tripRecord.getDurationSecs()).isEqualTo(3600);
    Assertions.assertThat(tripRecord.getFromStopId()).isEqualTo("Stop1");
    Assertions.assertThat(tripRecord.getToStopId()).isEqualTo("Stop2");
    Assertions.assertThat(tripRecord.getChargeAmount()).isEqualTo(2.50);
    Assertions.assertThat(tripRecord.getCompanyId()).isEqualTo("Company1");
    Assertions.assertThat(tripRecord.getBusID()).isEqualTo("Bus37");
    Assertions.assertThat(tripRecord.getPan()).isEqualTo("5500005555555559");
    Assertions.assertThat(tripRecord.getStatus()).isEqualTo("COMPLETED");
  }

  @Test
  void testToString() {
    OffsetDateTime now = OffsetDateTime.now();
    TripRecord tripRecord = new TripRecord();
    tripRecord.setStarted(now);
    tripRecord.setFinished(now);
    tripRecord.setDurationSecs(3600);
    tripRecord.setFromStopId("Stop1");
    tripRecord.setToStopId("Stop2");
    tripRecord.setChargeAmount(2.50);
    tripRecord.setCompanyId("Company1");
    tripRecord.setBusID("Bus37");
    tripRecord.setPan("5500005555555559");
    tripRecord.setStatus("COMPLETED");

    String expectedString =
        String.format(
            TRIP_RECORD,
            now,
            now,
            3600,
            "Stop1",
            "Stop2",
            2.50,
            "Company1",
            "Bus37",
            "5500005555555559",
            "COMPLETED");

    Assertions.assertThat(tripRecord).hasToString(expectedString);
  }
}
