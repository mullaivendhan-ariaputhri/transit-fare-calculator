package com.littlepay.fare.service;

import com.littlepay.fare.dto.TapRecord;
import com.littlepay.fare.dto.TripRecord;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FareService {

  @Autowired TapService tapService;

  @Autowired TripService tripService;

  public void tapsToTrips() {
    // Load Tap details
    log.info("Loading Tap details");
    List<TapRecord> taps = tapService.getTapRecords();
    log.info("Taps Loaded from file, Count # - {}", taps.size());
    log.debug("Taps - {}", taps);

    // Generate Trips from Taps
    log.info("Generate Trips from Taps");
    List<TripRecord> trips = tripService.generateTripsFromTaps(taps);
    log.info("Trips generated from Taps, Count # - {}", trips.size());
    log.debug("Trips - {}", trips);

    // Log Trips
    log.info("Write Trips to file");
    boolean success = tripService.writeTrips(trips);
    if (success) {
      log.info("Trips logged successfully!");
    } else {
      // Integrate Error management
      log.error("Failure in logging trips, please check the logs.");
    }
  }
}
