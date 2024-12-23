package com.example.springbootgraphql.listener;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * LoggingListener is a Servlet Listener for listening to the GraphQL request. It provides hooks
 * into the servlet request execution (request, success, error, and finally).
 *
 * <p>The Servlet Listener listens to the servlet request, but not to the GraphQL query execution.
 * If you want to listen to that (pre/post resolver methods etc) you should use the Instrumentation
 * provided by GraphQL Java.
 *
 * <p>This listener is shared among threads, which is why we don't save any state in the listener
 * object. Here a new callback is returned per method invocation. If we want to save some state on
 * the thread, we would implement the use of ThreadLocal.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingListener implements GraphQLServletListener {

  private final Clock clock;

  /**
   * Log the completed GraphQL request execution time.
   *
   * @param request {@link HttpServletRequest} the HTTP request
   * @param response {@link HttpServletResponse} the HTTP response
   * @return {@link RequestCallback} a callback to handle request lifecycle events
   */
  @Override
  public RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {

    var startTime = Instant.now(clock);
    // log.info("Received GraphQL request.");

    return new RequestCallback() {
      @Override
      public void onSuccess(HttpServletRequest request, HttpServletResponse response) {
        // no-op
        // RequestCallback.super.onSuccess(request, response);
      }

      /**
       * Handle errors during the request.
       *
       * @param request {@link HttpServletRequest} the HTTP request
       * @param response {@link HttpServletResponse} the HTTP response
       * @param throwable the exception thrown during request processing
       */
      @Override
      public void onError(
          HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
        // no-op
        // RequestCallback.super.onError(request, response, throwable);
        log.error("Caught exception in listener.", throwable);
      }

      /**
       * Perform final actions after the request is completed.
       *
       * @param request {@link HttpServletRequest} the HTTP request
       * @param response {@link HttpServletResponse} the HTTP response
       */
      @Override
      public void onFinally(HttpServletRequest request, HttpServletResponse response) {
        // RequestCallback.super.onFinally(request, response);
        //        log.info(
        //            "Completed Request. Time Taken: {}", Duration.between(startTime,
        // Instant.now(clock)));
        /**
         * This callback will be called post graphql lifecycle.
         * If we are multi-threading we can clear the original NIO thread MDC variables here.
         */
        MDC.clear();
      }
    };
  }
}
