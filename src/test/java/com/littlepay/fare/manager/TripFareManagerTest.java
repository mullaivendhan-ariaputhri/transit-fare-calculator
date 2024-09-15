package com.littlepay.fare.manager;

import static org.mockito.Mockito.when;

import com.littlepay.fare.config.FareConfig;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TripFareManagerTest {

  @Mock private FareConfig fareConfig;

  @InjectMocks private TripFareManager tripFareManager;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    // FareConfig
    List<FareConfig.FareMapping> fareMappings = new ArrayList<>();
    FareConfig.FareMapping mapping1 = new FareConfig.FareMapping();
    mapping1.setFrom("Stop1");
    mapping1.setTo("Stop2");
    mapping1.setCost(3.25);

    FareConfig.FareMapping mapping2 = new FareConfig.FareMapping();
    mapping2.setFrom("Stop2");
    mapping2.setTo("Stop3");
    mapping2.setCost(5.50);

    FareConfig.FareMapping mapping3 = new FareConfig.FareMapping();
    mapping3.setFrom("Stop1");
    mapping3.setTo("Stop3");
    mapping3.setCost(7.30);

    FareConfig.FareMapping mapping4 = new FareConfig.FareMapping();
    mapping4.setFrom("Stop2");
    mapping4.setTo("Stop1");
    mapping4.setCost(3.25);

    FareConfig.FareMapping mapping5 = new FareConfig.FareMapping();
    mapping5.setFrom("Stop3");
    mapping5.setTo("Stop2");
    mapping5.setCost(5.50);

    FareConfig.FareMapping mapping6 = new FareConfig.FareMapping();
    mapping6.setFrom("Stop3");
    mapping6.setTo("Stop1");
    mapping6.setCost(7.30);

    fareMappings.add(mapping1);
    fareMappings.add(mapping2);
    fareMappings.add(mapping3);
    fareMappings.add(mapping4);
    fareMappings.add(mapping5);
    fareMappings.add(mapping6);

    when(fareConfig.getMapping()).thenReturn(fareMappings);

    // Initialize TripFareManager with mocked config
    tripFareManager = new TripFareManager(fareConfig);
  }

  @Test
  void getFare() {
    // Test direct route
    Assertions.assertThat(tripFareManager.getFare("Stop1", "Stop2")).isEqualTo(3.25);
    Assertions.assertThat(tripFareManager.getFare("Stop2", "Stop3")).isEqualTo(5.50);
    Assertions.assertThat(tripFareManager.getFare("Stop1", "Stop3")).isEqualTo(7.30);

    // Test reverse route
    Assertions.assertThat(tripFareManager.getFare("Stop2", "Stop1")).isEqualTo(3.25);
    Assertions.assertThat(tripFareManager.getFare("Stop3", "Stop2")).isEqualTo(5.50);
    Assertions.assertThat(tripFareManager.getFare("Stop3", "Stop1")).isEqualTo(7.30);

    // Test non-existing route
    Assertions.assertThat(tripFareManager.getFare("Stop1", "Stop4")).isEqualTo(0.0);
  }

  @Test
  void testGetMaxFare() {
    // Test maximum fare for a given start stop
    Assertions.assertThat(tripFareManager.getMaxFare("Stop1")).isEqualTo(7.30);
    Assertions.assertThat(tripFareManager.getMaxFare("Stop2")).isEqualTo(5.50);
    Assertions.assertThat(tripFareManager.getMaxFare("Stop3")).isEqualTo(7.30);
  }
}
