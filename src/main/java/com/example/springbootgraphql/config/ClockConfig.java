package com.example.springbootgraphql.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Instead of using .now() for local, offset, zone date times,
// we can call .now(Clock) which means we can mock the clock's response
// in tests/integration tests etc without having to worry about exact
// runtime values

@Configuration
public class ClockConfig {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
