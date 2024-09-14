package com.littlepay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FareCalculatorApp {

  public static void main(String[] args) {
    log.debug("FareCalculatorApp Started");
    SpringApplication.run(FareCalculatorApp.class, args);
  }
}
