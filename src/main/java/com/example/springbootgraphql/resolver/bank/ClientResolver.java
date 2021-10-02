package com.example.springbootgraphql.resolver.bank;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import graphql.GraphQLException;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
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
  private final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  /**
   * client
   *
   * <p>Execute asynchronously by setting the return type to a CompletableFuture and executing the
   * logic within another thread.
   *
   * @param bankAccount the {@link BankAccount instance being resolved}
   * @return a new {@link CompletableFuture} that is asynchronously completed by a task running in
   *     the given {@code executorService} with the value obtained by calling the given {@code
   *     Supplier – a function returning the value to be used to complete the returned
   *     CompletableFuture}.
   */
  public CompletableFuture<Client> client(BankAccount bankAccount) {
    log.info("Stop me debugging");

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
