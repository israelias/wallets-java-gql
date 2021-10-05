package com.example.springbootgraphql.instrumentation;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RequestLoggingInstrumentation extends {@link SimpleInstrumentation} to allow us to look at the
 * query coming in and add any custom piece of code at various times
 *
 * @see graphql.execution.instrumentation.Instrumentation Instrumentation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingInstrumentation extends SimpleInstrumentation {
  private final Clock clock;

  /**
   * This is called right at the start of query execution and is the first step in the
   * instrumentation chain.
   *
   * @param parameters {@link InstrumentationExecutionParameters} the parameters to this step
   * @return a {@link InstrumentationContext} object that runs a callback {@code whenCompleted()}
   *     when the step ends
   */
  @Override
  public InstrumentationContext<ExecutionResult> beginExecution(
      InstrumentationExecutionParameters parameters) {

    /** 1: Logs the start time. */
    var start = Instant.now(clock);

    /**
     * 2: Get the unique {@code id} that will be or was used to execute this operation via an {@link
     * graphql.ExecutionInput ExecutionInput}
     */
    var executionId = parameters.getExecutionInput().getExecutionId();

    /** 3: Log the full query */
    log.info(
        "{}: query: {} with variables: {}",
        executionId,
        parameters.getQuery(),
        parameters.getVariables());

    return SimpleInstrumentationContext.whenCompleted(
        (executionResult, throwable) -> {
          var duration = Duration.between(start, Instant.now(clock));
          if (throwable == null) {

            log.info("{}: completed successfully in: {}", executionId, duration);
          } else {
            log.warn("{}: failed in {}", executionId, duration, throwable);
          }
        });
  }
}
