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
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * RequestLoggingInstrumentation extends {@link SimpleInstrumentation} to allow us to look at the
 * query coming in and add any custom piece of code at various times
 *
 * @see graphql.execution.instrumentation.Instrumentation Instrumentation
 * @see org.springframework.boot.logging.logback default.xml, console-appender
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingInstrumentation extends SimpleInstrumentation {

  public static String CORRELATION_ID = "correlation_id";
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
     * 2a: Get the unique {@code id} that will be or was used to execute this operation via an
     * {@link graphql.ExecutionInput ExecutionInput}
     *
     * <p>Removed as we are adding it to every log var executionId =
     * parameters.getExecutionInput().getExecutionId();
     */

    /**
     * 2b: Initialize the first key-value pair by adding {@code executionId} into the {@link MDC}
     * map, which belongs to the `logger`, which is tied to the `neo Thread`
     *
     * <p>We take the executionId of the graphqlQuery because in this instance, we are the `edge
     * service`, if we can think of a vertical line on this editor, when they call us, our service
     * attaches the node head, we propagate down the line through external services and until this
     * line meets us again at the very edge, where we clear the MDC.
     *
     * <p>Thus we are using the executionID because it is assigned from the graphql server.
     * Otherwise you can take it from the request header!
     *
     *
     */
    MDC.put(CORRELATION_ID, parameters.getExecutionInput().getExecutionId().toString());

    /** 3: Log the full query */
    log.info("Query: {} with variables: {}", parameters.getQuery(), parameters.getVariables());

    return SimpleInstrumentationContext.whenCompleted(
        (executionResult, throwable) -> {
          var duration = Duration.between(start, Instant.now(clock));
          if (throwable == null) {
            log.info("Completed successfully in: {}", duration);
          } else {
            log.warn("Failed in {}", duration, throwable);
          }
          /**
           * If we have async resolvers, this callback can occur in the thread-pool and not the NIO
           * thread. In that case, the `LoggingListener` will be used as a fallback to clear the NIO
           * thread.
           */
          MDC.clear();
        });
  }
}
