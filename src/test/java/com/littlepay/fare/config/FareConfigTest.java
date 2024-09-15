package com.littlepay.fare.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class FareConfigTest {

  @Autowired private FareConfig fareConfig;

  private FareConfig mockFareConfig;

  @BeforeEach
  public void setUp() {
    mockFareConfig = new FareConfig();

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

    fareMappings.add(mapping1);
    fareMappings.add(mapping2);
    fareMappings.add(mapping3);

    mockFareConfig.setMapping(fareMappings);
  }

  @Test
  void testFareMappings() {
    validateFareMappings(mockFareConfig);
  }

  @Test
  void testFareMappingsFromConfig() {
    validateFareMappings(fareConfig);
  }

  // Validates fare mapping details
  void validateFareMappings(FareConfig fareConfig) {
    assertThat(mockFareConfig.getMapping()).isNotNull();
    assertThat(mockFareConfig.getMapping()).hasSize(3);
    FareConfig.FareMapping mapping1 = fareConfig.getMapping().get(0);
    assertThat(mapping1.getFrom()).isEqualTo("Stop1");
    assertThat(mapping1.getTo()).isEqualTo("Stop2");
    assertThat(mapping1.getCost()).isEqualTo(3.25);

    FareConfig.FareMapping mapping2 = fareConfig.getMapping().get(1);
    assertThat(mapping2.getFrom()).isEqualTo("Stop2");
    assertThat(mapping2.getTo()).isEqualTo("Stop3");
    assertThat(mapping2.getCost()).isEqualTo(5.50);

    FareConfig.FareMapping mapping3 = fareConfig.getMapping().get(2);
    assertThat(mapping3.getFrom()).isEqualTo("Stop1");
    assertThat(mapping3.getTo()).isEqualTo("Stop3");
    assertThat(mapping3.getCost()).isEqualTo(7.30);
  }
}
