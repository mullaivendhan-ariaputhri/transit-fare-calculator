package com.littlepay.fare.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TapRecord;
import com.littlepay.fare.dto.TripRecord;
import com.littlepay.fare.exception.FareCalculatorException;
import com.littlepay.fare.manager.TripFareManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TripServiceTest {

  @Mock private AppConfig appConfig;

  @Mock private TripFareManager tripFareManager;

  @InjectMocks private TripService tripService;

  @InjectMocks private TapService tapService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGenerateTripsFromTapsCSV() throws FareCalculatorException {
    // taps.csv - Test with full file and evaluate total trips
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTapsFilePath()).thenReturn("src/test/resources/taps.csv");
    when(appConfig.getTapsHeaders())
        .thenReturn(
            new String[] {"ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"});
    List<TapRecord> taps = tapService.getTapRecords();

    // Validate the taps
    assertEquals(6, taps.size());

    // Routes
    when(tripFareManager.getFare("Stop1", "Stop2")).thenReturn(3.25);
    when(tripFareManager.getFare("Stop2", "Stop3")).thenReturn(5.50);
    when(tripFareManager.getFare("Stop1", "Stop3")).thenReturn(7.30);
    when(tripFareManager.getFare("Stop2", "Stop1")).thenReturn(3.25);
    when(tripFareManager.getFare("Stop3", "Stop2")).thenReturn(5.50);
    when(tripFareManager.getFare("Stop3", "Stop1")).thenReturn(7.30);

    // When - Invoke Trips from Taps
    List<TripRecord> trips = tripService.generateTripsFromTaps(taps);

    // Then
    assertEquals(4, trips.size());
    TripRecord trip = trips.get(0);
    assertEquals(OffsetDateTime.parse("2023-01-23T08:00:00Z"), trip.getStarted());
    assertEquals(OffsetDateTime.parse("2023-01-23T08:02:00Z"), trip.getFinished());
    assertEquals(120, trip.getDurationSecs());
    assertEquals("Stop1", trip.getFromStopId());
    assertEquals("Stop1", trip.getToStopId());
    assertEquals(0.00, trip.getChargeAmount());
    assertEquals("Company1", trip.getCompanyId());
    assertEquals("Bus37", trip.getBusID());
    assertEquals("4111111111111111", trip.getPan());
    assertEquals("CANCELLED", trip.getStatus());
  }

  @Test
  void testGenerateTripsCompleted() {
    // Given
    TapRecord tapOn =
        TapRecord.builder()
            .id(1)
            .dateTimeUTC(OffsetDateTime.parse("2023-01-22T13:00:00Z"))
            .tapType("ON")
            .stopId("Stop1")
            .companyId("Company1")
            .busId("Bus37")
            .pan("5500005555555559")
            .build();

    TapRecord tapOff =
        TapRecord.builder()
            .id(2)
            .dateTimeUTC(OffsetDateTime.parse("2023-01-22T13:05:00Z"))
            .tapType("OFF")
            .stopId("Stop2")
            .companyId("Company1")
            .busId("Bus37")
            .pan("5500005555555559")
            .build();

    List<TapRecord> taps = Arrays.asList(tapOn, tapOff);

    when(tripFareManager.getFare(anyString(), anyString())).thenReturn(3.25);

    // When
    List<TripRecord> trips = tripService.generateTripsFromTaps(taps);

    // Then
    assertEquals(1, trips.size());
    TripRecord trip = trips.get(0);
    assertEquals(tapOn.getDateTimeUTC(), trip.getStarted());
    assertEquals(tapOff.getDateTimeUTC(), trip.getFinished());
    assertEquals(300, trip.getDurationSecs());
    assertEquals("Stop1", trip.getFromStopId());
    assertEquals("Stop2", trip.getToStopId());
    assertEquals(3.25, trip.getChargeAmount());
    assertEquals("Company1", trip.getCompanyId());
    assertEquals("Bus37", trip.getBusID());
    assertEquals("5500005555555559", trip.getPan());
    assertEquals("COMPLETED", trip.getStatus());
  }

  @Test
  void testGenerateTripsCancelled() {
    // Given
    TapRecord tapOn =
        TapRecord.builder()
            .id(1)
            .dateTimeUTC(OffsetDateTime.parse("2023-01-22T13:00:00Z"))
            .tapType("ON")
            .stopId("Stop1")
            .companyId("Company1")
            .busId("Bus37")
            .pan("5500005555555559")
            .build();

    TapRecord tapOff =
        TapRecord.builder()
            .id(2)
            .dateTimeUTC(OffsetDateTime.parse("2023-01-22T13:01:00Z"))
            .tapType("OFF")
            .stopId("Stop1")
            .companyId("Company1")
            .busId("Bus37")
            .pan("5500005555555559")
            .build();

    List<TapRecord> taps = Arrays.asList(tapOn, tapOff);

    when(tripFareManager.getFare(anyString(), anyString())).thenReturn(0.0);

    // When
    List<TripRecord> trips = tripService.generateTripsFromTaps(taps);

    // Then
    assertEquals(1, trips.size());
    TripRecord trip = trips.get(0);
    assertEquals(tapOn.getDateTimeUTC(), trip.getStarted());
    assertEquals(tapOff.getDateTimeUTC(), trip.getFinished());
    assertEquals(60, trip.getDurationSecs());
    assertEquals("Stop1", trip.getFromStopId());
    assertEquals("Stop1", trip.getToStopId());
    assertEquals(0.0, trip.getChargeAmount());
    assertEquals("Company1", trip.getCompanyId());
    assertEquals("Bus37", trip.getBusID());
    assertEquals("5500005555555559", trip.getPan());
    assertEquals("CANCELLED", trip.getStatus());
  }

  @Test
  void testGenerateTripsIncomplete() {
    // Given
    TapRecord tapOn =
        TapRecord.builder()
            .id(1)
            .dateTimeUTC(OffsetDateTime.parse("2023-01-22T13:00:00Z"))
            .tapType("ON")
            .stopId("Stop1")
            .companyId("Company1")
            .busId("Bus37")
            .pan("5500005555555559")
            .build();

    List<TapRecord> taps = Collections.singletonList(tapOn);

    when(tripFareManager.getMaxFare(anyString())).thenReturn(7.30);

    // When
    List<TripRecord> trips = tripService.generateTripsFromTaps(taps);

    // Then
    assertEquals(1, trips.size());
    TripRecord trip = trips.get(0);
    assertEquals(tapOn.getDateTimeUTC(), trip.getStarted());
    assertNull(trip.getFinished());
    assertNull(trip.getDurationSecs());
    assertEquals("Stop1", trip.getFromStopId());
    assertNull(trip.getToStopId());
    assertEquals(7.30, trip.getChargeAmount());
    assertEquals("Company1", trip.getCompanyId());
    assertEquals("Bus37", trip.getBusID());
    assertEquals("5500005555555559", trip.getPan());
    assertEquals("INCOMPLETE", trip.getStatus());
  }

  @Test
  void testGenerateTripsInvalid() {
    // Given
    TapRecord tapOff =
        TapRecord.builder()
            .id(2)
            .dateTimeUTC(OffsetDateTime.parse("2023-01-22T13:01:00Z"))
            .tapType("OFF")
            .stopId("Stop1")
            .companyId("Company1")
            .busId("Bus37")
            .pan("5500005555555559")
            .build();

    List<TapRecord> taps = Collections.singletonList(tapOff);

    when(tripFareManager.getFare(anyString(), anyString())).thenReturn(0.0);

    // When
    List<TripRecord> trips = tripService.generateTripsFromTaps(taps);

    // Then
    assertEquals(1, trips.size());
    TripRecord trip = trips.get(0);
    assertNull(trip.getStarted());
    assertEquals(tapOff.getDateTimeUTC(), trip.getFinished());
    assertNull(trip.getDurationSecs());
    assertNull(trip.getFromStopId());
    assertEquals("Stop1", trip.getToStopId());
    assertNull(trip.getChargeAmount());
    assertEquals("Company1", trip.getCompanyId());
    assertEquals("Bus37", trip.getBusID());
    assertEquals("5500005555555559", trip.getPan());
    assertEquals("INVALID", trip.getStatus());
  }

  @Test
  void testWriteTripsSuccess() throws IOException, FareCalculatorException {
    Path tempFile = Files.createTempFile("test", ".csv");
    when(appConfig.getTripsFilePath()).thenReturn(tempFile.toString());
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTripsHeaders())
        .thenReturn(
            new String[] {
              "Started",
              "Finished",
              "DurationSecs",
              "FromStopId",
              "ToStopId",
              "ChargeAmount",
              "CompanyId",
              "BusID",
              "PAN",
              "Status"
            });

    // Given
    TripRecord trip = new TripRecord();
    trip.setStarted(OffsetDateTime.parse("2023-01-22T13:00:00Z"));
    trip.setFinished(OffsetDateTime.parse("2023-01-22T13:05:00Z"));
    trip.setDurationSecs(300);
    trip.setFromStopId("Stop1");
    trip.setToStopId("Stop2");
    trip.setChargeAmount(3.25);
    trip.setCompanyId("Company1");
    trip.setBusID("Bus37");
    trip.setPan("5500005555555559");
    trip.setStatus("COMPLETED");

    List<TripRecord> trips = List.of(trip);

    // When
    boolean result = tripService.writeTrips(trips);

    // Then
    assertTrue(result, "Trips should be written successfully");
    assertTrue(Files.exists(tempFile), "File should exist");
    long fileSize = Files.size(tempFile);
    assertTrue(fileSize > 0, "File size should be greater than 0");
  }

  @Test
  void testWriteTripsFailure() throws FareCalculatorException {
    // When
    when(appConfig.getTripsFilePath()).thenReturn("Z:/test.csv");
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTripsHeaders())
        .thenReturn(
            new String[] {
              "Started",
              "Finished",
              "DurationSecs",
              "FromStopId",
              "ToStopId",
              "ChargeAmount",
              "CompanyId",
              "BusID",
              "PAN",
              "Status"
            });

    // Given
    TripRecord trip = new TripRecord();
    trip.setStarted(OffsetDateTime.parse("2023-01-22T13:00:00Z"));
    trip.setFinished(OffsetDateTime.parse("2023-01-22T13:05:00Z"));
    trip.setDurationSecs(300);
    trip.setFromStopId("Stop1");
    trip.setToStopId("Stop2");
    trip.setChargeAmount(3.25);
    trip.setCompanyId("Company1");
    trip.setBusID("Bus37");
    trip.setPan("5500005555555559");
    trip.setStatus("COMPLETED");

    List<TripRecord> trips = List.of(trip);
    try {
      tripService.writeTrips(trips);
    } catch (Exception e) {
      // Then
      assertThat(e).isInstanceOf(FareCalculatorException.class);
    }
  }
}
