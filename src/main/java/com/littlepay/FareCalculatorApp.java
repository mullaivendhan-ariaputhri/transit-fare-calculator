package com.littlepay;

import com.littlepay.fare.service.FareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FareCalculatorApp implements CommandLineRunner {

  @Autowired FareService fareService;

  public static void main(String[] args) {
    log.debug("FareCalculatorApp Started, In main");
    SpringApplication.run(FareCalculatorApp.class, args);
  }

  @Override
  public void run(String... args) {
    fareService.tapsToTrips();
  }
}
