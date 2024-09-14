package com.littlepay.fare.enums;

public enum TripType {
  COMPLETED("Completed"),
  INCOMPLETE("Incomplete"),
  CANCELLED("Cancelled");

  private final String tripType;

  TripType(String tripType) {
    this.tripType = tripType;
  }

  public String getTripType() {
    return tripType;
  }

  public boolean isTripComplete() {
    return this == COMPLETED;
  }

  public boolean isTripInComplete() {
    return this == INCOMPLETE;
  }

  public boolean isTripCancelled() {
    return this == CANCELLED;
  }
}
