package com.example.springbootgraphql.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClockConfig
 *
 * <p>Instead of using .now() for local, offset, zone date times, we can call .now(Clock) in order
 * to mock the clock's response in unit/integration tests without having to worry about exact
 * runtime values
 */
@Configuration
public class ClockConfig {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
