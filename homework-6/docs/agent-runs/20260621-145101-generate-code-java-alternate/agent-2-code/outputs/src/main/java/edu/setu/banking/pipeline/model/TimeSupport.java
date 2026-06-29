package edu.setu.banking.pipeline.model;

import java.time.Clock;
import java.time.Instant;

public final class TimeSupport {
  private static Clock clock = Clock.systemUTC();

  private TimeSupport() {}

  public static String now() {
    return Instant.now(clock).toString();
  }

  public static void setClockForTests(Clock replacement) {
    clock = replacement;
  }
}
