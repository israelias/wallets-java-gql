package com.example.springbootgraphql.util;

import static com.example.springbootgraphql.instrumentation.RequestLoggingInstrumentation.CORRELATION_ID;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

@RequiredArgsConstructor
public class CorrelationIdPropagationExecutor implements Executor {

  // delegate main executor
  private final Executor delegate;

  public static Executor wrap(Executor executor) {
    return new CorrelationIdPropagationExecutor(executor);
  }

  /**
   * Executes the given command at some time in the future. The command may execute in a new thread,
   * in a pooled thread, or in the calling thread, at the discretion of the {@code Executor}
   * implementation.
   *
   * <p>Used to propagate the `correlation_id` to the executor
   *
   * @param command the runnable task
   * @throws RejectedExecutionException if this task cannot be accepted for execution
   * @throws NullPointerException if command is null
   */
  @Override
  public void execute(@NotNull Runnable command) {
    var correlationId = MDC.get(CORRELATION_ID);

    delegate.execute(
        () -> {
          try {
            MDC.put(CORRELATION_ID, correlationId);
            command.run();

          } finally {
            MDC.remove(CORRELATION_ID);
          }
        });

  }
}
