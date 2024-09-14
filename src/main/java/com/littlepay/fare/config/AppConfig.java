package com.littlepay.fare.config;

import com.littlepay.fare.constants.Constants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@Slf4j
public class AppConfig {
  @Value("${date.format}")
  private String dateFormat;

  @Value("${timezone}")
  private String timeZone;

  @Value("${taps.file.path}")
  private String tapsFilePath;

  @Value("${taps.headers}")
  private String tapsHeaders;

  @Value("${trips.file.path}")
  private String tripsFilePath;

  @Value("${trips.headers}")
  private String tripsHeaders;

  // Taps CSV headers
  public String[] getTapsHeaders() {
    return tapsHeaders.split(Constants.COMMA_SEPARATOR);
  }

  // Trips CSV headers
  public String[] getTripsHeaders() {
    return tripsHeaders.split(Constants.COMMA_SEPARATOR);
  }
}
