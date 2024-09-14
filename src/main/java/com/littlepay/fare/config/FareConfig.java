package com.littlepay.fare.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "fare")
public class FareConfig {
  private List<FareMapping> mapping;

  public static class FareMapping {
    private String from;
    private String to;
    private Double cost;

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public String getTo() {
      return to;
    }

    public void setTo(String to) {
      this.to = to;
    }

    public Double getCost() {
      return cost;
    }

    public void setCost(Double cost) {
      this.cost = cost;
    }
  }
}
