package com.littlepay.fare.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.littlepay.fare.dto.TapRecord;
import com.littlepay.fare.dto.TripRecord;
import com.littlepay.fare.exception.FareCalculatorException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FareServiceTest {

  @Mock private TapService tapService;

  @Mock private TripService tripService;

  @InjectMocks private FareService fareService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testTapsToTripsSuccess() throws FareCalculatorException {
    // Given
    List<TapRecord> mockTaps = new ArrayList<>();
    List<TripRecord> mockTrips = new ArrayList<>();
    when(tapService.getTapRecords()).thenReturn(mockTaps);
    when(tripService.generateTripsFromTaps(mockTaps)).thenReturn(mockTrips);
    when(tripService.writeTrips(mockTrips)).thenReturn(true);

    // When
    fareService.tapsToTrips();

    // Then
    verify(tapService).getTapRecords();
    verify(tripService).generateTripsFromTaps(mockTaps);
    verify(tripService).writeTrips(mockTrips);
  }

  @Test
  void testTapsToTripsFailureInLogging() throws FareCalculatorException {
    // Given
    List<TapRecord> mockTaps = new ArrayList<>();
    List<TripRecord> mockTrips = new ArrayList<>();
    when(tapService.getTapRecords()).thenReturn(mockTaps);
    when(tripService.generateTripsFromTaps(mockTaps)).thenReturn(mockTrips);
    when(tripService.writeTrips(mockTrips)).thenReturn(false);

    // When
    fareService.tapsToTrips();

    // Then
    verify(tapService).getTapRecords();
    verify(tripService).generateTripsFromTaps(mockTaps);
    verify(tripService).writeTrips(mockTrips);
  }
}
