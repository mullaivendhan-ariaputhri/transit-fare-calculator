package com.littlepay.fare.exception;

public class FareCalculatorException extends Exception {
  public FareCalculatorException() {
    super();
  }

  public FareCalculatorException(String message) {
    super(message);
  }

  public FareCalculatorException(String message, Throwable cause) {
    super(message, cause);
  }

  public FareCalculatorException(Throwable cause) {
    super(cause);
  }
}
