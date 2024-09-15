package com.littlepay;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.littlepay.fare.service.FareService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("default")
class FareCalculatorAppTest {

  @MockBean private FareService fareService;

  @InjectMocks private FareCalculatorApp fareCalculatorApp;

  @Autowired private ApplicationContext applicationContext;

  @Test
  public void testApplicationContextLoads() {
    // Ensure that the application context loads and the FareCalculatorApp bean is available
    FareCalculatorApp fareCalculatorApp = applicationContext.getBean(FareCalculatorApp.class);
    assertNotNull(fareCalculatorApp);
  }

  @Test
  public void testRunMethodCallsFareService() {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Run the application
    fareCalculatorApp.run();

    // Verify that fareService.tapsToTrips() was called exactly once
    verify(fareService, times(1)).tapsToTrips();
  }
}
