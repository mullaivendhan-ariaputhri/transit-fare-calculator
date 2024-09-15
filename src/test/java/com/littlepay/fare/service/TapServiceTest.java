package com.littlepay.fare.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import com.littlepay.fare.config.AppConfig;
import com.littlepay.fare.dto.TapRecord;
import com.littlepay.fare.exception.FareCalculatorException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
class TapServiceTest {

  @Mock private AppConfig appConfig;

  @InjectMocks private TapService tapService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetTapRecordsSuccess() {
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTapsFilePath()).thenReturn("src/test/resources/taps.csv");
    when(appConfig.getTapsHeaders())
        .thenReturn(
            new String[] {"ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"});

    try {
      // Override the default parsing mechanism to use the in-memory data
      List<TapRecord> taps = tapService.getTapRecords();

      // Validate the results
      assertEquals(6, taps.size());

      TapRecord firstTap = taps.get(0);
      assertEquals(1, firstTap.getId());
      assertEquals(
          OffsetDateTime.of(2023, 1, 22, 13, 0, 0, 0, ZoneOffset.UTC), firstTap.getDateTimeUTC());
      assertEquals("ON", firstTap.getTapType());
      assertEquals("Stop1", firstTap.getStopId());
      assertEquals("Company1", firstTap.getCompanyId());
      assertEquals("Bus37", firstTap.getBusId());
      assertEquals("5500005555555559", firstTap.getPan());
    } catch (FareCalculatorException e) {
      fail("Exception should not have occurred since file exists");
    }
  }

  @Test
  void testGetTapRecordsFileNotExists() {
    // Given
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTapsFilePath()).thenReturn("dummy/path/taps.csv");

    try {
      // When
      tapService.getTapRecords();
    } catch (Exception e) {
      // Then
      assertThat(e).isInstanceOf(FareCalculatorException.class);
    }
  }

  @Test
  void testGetTapRecordsEmptyFile() {
    // Given
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTapsFilePath()).thenReturn("src/test/resources/empty-file.csv");
    when(appConfig.getTapsHeaders())
        .thenReturn(
            new String[] {"ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"});

    try {
      // When
      tapService.getTapRecords();
    } catch (Exception e) {
      // Then
      assertThat(e).isInstanceOf(FareCalculatorException.class);
    }
  }

  @Test
  void testGetTapRecordsMissingColumns() {
    // Given
    when(appConfig.getDateFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
    when(appConfig.getTapsFilePath()).thenReturn("src/test/resources/missing-columns.csv");
    when(appConfig.getTapsHeaders())
        .thenReturn(
            new String[] {"ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"});

    try {
      // When
      tapService.getTapRecords();
    } catch (Exception e) {
      // Then
      assertThat(e).isInstanceOf(FareCalculatorException.class);
    }
  }
}
