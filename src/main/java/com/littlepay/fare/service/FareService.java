package com.littlepay.fare.service;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TapRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FareService {

  @Autowired AppConfig appConfig;

  @Autowired TapService tapService;

  @Autowired TripService tripService;

  public void calculateTripFare() {
    // Tap details
    List<TapRecord> tapRecords = tapService.getTapRecords();
    tapRecords.forEach(tapRecord -> log.debug(tapRecord.toString()));

    // Trip details
  }
}
