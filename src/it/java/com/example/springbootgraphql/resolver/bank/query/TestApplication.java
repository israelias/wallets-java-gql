package com.example.springbootgraphql.resolver.bank.query;

import static java.time.ZoneOffset.UTC;

import com.example.springbootgraphql.Application;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/** TestApplication configurations for dynamic values. */
@Configuration
@Import(Application.class)
public class TestApplication {

  /**
   * Configure a fixed time TODO Do the same for other dynamic values
   *
   * @return a fixed {@link Clock} instant with the instant to use as the clock, not null zone – the
   *     time-zone to use to convert the instant to date-time, not nul
   */
  @Bean
  @Primary
  public Clock testClock() {
    return Clock.fixed(LocalDate.of(2021, 10, 6).atStartOfDay(UTC).toInstant(), UTC);
  }
}
