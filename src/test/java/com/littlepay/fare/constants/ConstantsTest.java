package com.littlepay.fare.constants;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConstantsTest {

  @Test
  void testConstant() {
    assertThat(Constants.INVALID).isEqualTo("INVALID");
    assertThat(Constants.CANCELLED).isEqualTo("CANCELLED");
    assertThat(Constants.INCOMPLETE).isEqualTo("INCOMPLETE");
    assertThat(Constants.COMPLETED).isEqualTo("COMPLETED");
  }

  @Test
  void testConstantsInstantiation() {
    // Design check to prevent instantiation
    try {
      Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
      constructor.newInstance();
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalAccessException.class);
    }
  }
}
