package com.littlepay.fare.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.littlepay.fare.constants.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppConfigTest {

  @Autowired private AppConfig appConfig;

  @Test
  void testAppConfigs() {
    assertThat(appConfig.getDateFormat()).isNotNull();
    assertThat(appConfig.getTimeZone()).isNotNull();
    assertThat(appConfig.getTapsFilePath()).isNotNull();
    assertThat(appConfig.getTapsHeaders()).isNotNull();
    assertThat(appConfig.getTripsFilePath()).isNotNull();
    assertThat(appConfig.getTripsHeaders()).isNotNull();

    assertThat(appConfig.getDateFormat()).isEqualTo("dd-MM-yyyy HH:mm:ss");
    assertThat(appConfig.getTimeZone()).isEqualTo("UTC");
    assertThat(appConfig.getTapsFilePath()).isEqualTo("src/main/resources/taps.csv");
    assertThat(appConfig.getTapsHeaders())
        .isEqualTo(
            ("ID,DateTimeUTC,TapType,StopId,CompanyId,BusID,PAN").split(Constants.COMMA_SEPARATOR));
    assertThat(appConfig.getTapsHeaders()).hasSize(7);
    assertThat(appConfig.getTripsFilePath()).isEqualTo("target/trips.csv");
    assertThat(appConfig.getTripsHeaders())
        .isEqualTo(
            ("Started,Finished,DurationSecs,FromStopId,ToStopId,ChargeAmount,CompanyId,BusID,PAN,Status")
                .split(Constants.COMMA_SEPARATOR));
    assertThat(appConfig.getTripsHeaders()).hasSize(10);
  }
}
