package com.example.springbootgraphql.resolver.bank;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import com.example.springbootgraphql.util.CorrelationIdPropagationExecutor;
import graphql.GraphQLException;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.stereotype.Component;

/**
 * The ClientResolver implements {@link GraphQLResolver} with type {@link BankAccount} to resolve
 * the {@link Client} field within a {@link BankAccount}.
 */
@Slf4j
@Component
public class ClientResolver implements GraphQLResolver<BankAccount> {

  /**
   * executorService
   *
   * <p>The executor to use for asynchronous execution. {@link Executors#newFixedThreadPool(int)}
   *
   * <p>Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded
   * queue where:
   *
   * <ul>
   *   <li>initialized value {@code int} is the maximum number of processors available to the JVM
   *   <li>fetched by calling {@code availableProcessors()} on the runtime object associated with
   *       the current Java app.
   *   <li>accessed via {@code getRuntime()} on the app's single instance of the {@link Runtime
   *       class} environment.
   * </ul>
   *
   * @see java.util.concurrent.Executors
   * @see Runtime
   */
  private final Executor executorService =
      CorrelationIdPropagationExecutor.wrap(
          new DelegatingSecurityContextExecutorService(
              Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));

  /**
   * client
   *
   * <p>Execute asynchronously by setting the return type to a CompletableFuture and executing the
   * logic within another thread.
   *
   * <p>Method {@code client} must match the field name `client` in `bankAccount.graphqls`
   *
   * <p>BankAccount param passed from parent: as BankAccount resolves, it passes itself to its child
   * resolver `client` if it is included in the requested fields
   *
   * @param bankAccount the {@link BankAccount instance passed from the parent being resolved}
   * @return a new {@link CompletableFuture} that is asynchronously completed by a task running in
   *     the given {@code executorService} with the value obtained by calling the given {@code
   *     Supplier – a function returning the value to be used to complete the returned
   *     CompletableFuture}.
   */
  public CompletableFuture<Client> client(BankAccount bankAccount) {
    log.info("Stop me debugging");
    // TODO client should be a separate service to hook into to retrieve clients with different info
    return CompletableFuture.supplyAsync(
        () -> {
          log.info("Requesting client data for bank account id {}", bankAccount.getId());
          return Client.builder()
              .id(UUID.randomUUID())
              .firstName("Elias")
              .lastName("Wrubel")
              .build();
        },
        executorService);
  }
}
