package com.example.springbootgraphql.listener;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import java.time.Clock;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingListener implements GraphQLServletListener {

  private final Clock clock;

  @Override
  public RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {

    var startTime = Instant.now(clock);

    return new RequestCallback() {
      @Override
      public void onSuccess(HttpServletRequest request, HttpServletResponse response) {
        // no-op
        RequestCallback.super.onSuccess(request, response);
      }

      @Override
      public void onError(
          HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
        // no-op
        RequestCallback.super.onError(request, response, throwable);
      }

      @Override
      public void onFinally(HttpServletRequest request, HttpServletResponse response) {
        RequestCallback.super.onFinally(request, response);
      }
    };
  }
}
