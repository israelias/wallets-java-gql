package com.example.springbootgraphql.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorFactory {

  public static Executor newExecutor() {
    var processors = Runtime.getRuntime().availableProcessors();
    var executorService = Executors.newFixedThreadPool(processors);
    var securityDelegatingExecutor = new DelegatingSecurityContextExecutorService(executorService);
    return CorrelationIdPropagationExecutor.wrap(securityDelegatingExecutor);
  }
}
