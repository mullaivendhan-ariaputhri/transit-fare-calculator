package com.littlepay.fare.dto;

import static com.littlepay.fare.constants.Constants.TAP_RECORD;

import java.time.OffsetDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TapRecordTest {

  @Test
  public void testSettersAndGetters() {
    TapRecord tapRecord = new TapRecord();
    OffsetDateTime now = OffsetDateTime.now();
    tapRecord.setId(1);
    tapRecord.setDateTimeUTC(now);
    tapRecord.setTapType("ON");
    tapRecord.setStopId("Stop1");
    tapRecord.setCompanyId("Company1");
    tapRecord.setBusId("Bus37");
    tapRecord.setPan("5500005555555559");

    Assertions.assertThat(tapRecord.getId()).isEqualTo(1);
    Assertions.assertThat(tapRecord.getDateTimeUTC()).isEqualTo(now);
    Assertions.assertThat(tapRecord.getTapType()).isEqualTo("ON");
    Assertions.assertThat(tapRecord.getStopId()).isEqualTo("Stop1");
    Assertions.assertThat(tapRecord.getCompanyId()).isEqualTo("Company1");
    Assertions.assertThat(tapRecord.getBusId()).isEqualTo("Bus37");
    Assertions.assertThat(tapRecord.getPan()).isEqualTo("5500005555555559");
  }

  @Test
  public void testToString() {
    OffsetDateTime now = OffsetDateTime.now();
    TapRecord tapRecord = new TapRecord();
    tapRecord.setId(1);
    tapRecord.setDateTimeUTC(now);
    tapRecord.setTapType("ON");
    tapRecord.setStopId("Stop1");
    tapRecord.setCompanyId("Company1");
    tapRecord.setBusId("Bus37");
    tapRecord.setPan("5500005555555559");

    String expectedString =
        String.format(TAP_RECORD, 1, now, "ON", "Stop1", "Company1", "Bus37", "5500005555555559");

    Assertions.assertThat(tapRecord.toString()).isEqualTo(expectedString);
  }
}
