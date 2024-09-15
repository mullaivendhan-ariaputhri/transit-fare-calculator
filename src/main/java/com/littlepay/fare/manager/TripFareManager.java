package com.littlepay.fare.manager;

import static com.littlepay.fare.constants.Constants.HYPHEN_SEPARATOR;

import com.littlepay.fare.config.FareConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TripFareManager {
  private final Map<String, Double> fareMap;

  // Load fare config from properties and load as map (Both route and reverse route with same cost)
  public TripFareManager(FareConfig fareConfig) {
    fareMap = new HashMap<>();
    for (FareConfig.FareMapping mapping : fareConfig.getMapping()) {
      fareMap.put(getRoute(mapping.getFrom(), mapping.getTo()), mapping.getCost());
      fareMap.put(getRoute(mapping.getTo(), mapping.getFrom()), mapping.getCost());
    }
    log.info("Fare Map loaded {}", fareMap);
  }

  /**
   * Get fare between 2 stops
   *
   * @param fromStop From Stop
   * @param toStop To Stop
   * @return Fare between 2 stops
   */
  public double getFare(String fromStop, String toStop) {
    return fareMap.getOrDefault(getRoute(fromStop, toStop), 0.0);
  }

  /**
   * Get maximum fare if end stop is unknown
   *
   * @param fromStop From Stop
   * @return Max fare from stop
   */
  public double getMaxFare(String fromStop) {
    return fareMap.keySet().stream()
        .filter(route -> route.startsWith(fromStop))
        .map(fareMap::get)
        .max(Double::compare)
        .orElse(0.0);
  }

  private String getRoute(String fromStop, String toStop) {
    return fromStop.concat(HYPHEN_SEPARATOR).concat(toStop);
  }
}
