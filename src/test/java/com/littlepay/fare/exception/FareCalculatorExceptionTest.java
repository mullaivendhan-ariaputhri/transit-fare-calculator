package com.littlepay.fare.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class FareCalculatorExceptionTest {
  @Test
  void testDefaultConstructor() {
    // Test default constructor
    FareCalculatorException exception = new FareCalculatorException();
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructorWithMessage() {
    // Test constructor with a message
    String message = "Fare calculation error occurred";
    FareCalculatorException exception = new FareCalculatorException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructorWithMessageAndCause() {
    // Test constructor with a message and a cause
    String message = "Fare calculation error occurred";
    Throwable cause = new RuntimeException("Underlying error");
    FareCalculatorException exception = new FareCalculatorException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void testConstructorWithCause() {
    // Test constructor with a cause
    Throwable cause = new RuntimeException("Underlying error");
    FareCalculatorException exception = new FareCalculatorException(cause);
    assertEquals(cause, exception.getCause());
    assertEquals(cause.toString(), exception.getMessage());
  }
}
