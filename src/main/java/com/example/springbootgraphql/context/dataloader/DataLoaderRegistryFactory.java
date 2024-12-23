package com.example.springbootgraphql.context.dataloader;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.service.BalanceService;
import com.example.springbootgraphql.util.CorrelationIdPropagationExecutor;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.stereotype.Component;

/**
 * Factory for creating DataLoaderRegistry instances.
 *
 * <p>This factory is responsible for creating and configuring DataLoaderRegistry instances,
 * including the registration of specific DataLoaders such as the balance DataLoader.
 */
@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {

  private final BalanceService balanceService;
  public static final String BALANCE_DATA_LOADER = "BALANCE_DATA_LOADER";
  private static final Executor balanceThreadPool =
      CorrelationIdPropagationExecutor.wrap(
          new DelegatingSecurityContextExecutorService(
              Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));

  /**
   * Creates a DataLoaderRegistry for the specified user ID.
   *
   * <p>This method creates a new DataLoaderRegistry and registers the balance DataLoader
   * for the given user ID.
   *
   * @param userId the user ID for which to create the DataLoaderRegistry
   * @return a DataLoaderRegistry configured with the balance DataLoader
   */
  public DataLoaderRegistry create(String userId) {
    var registry = new DataLoaderRegistry();

    registry.register(BALANCE_DATA_LOADER, createBalanceDataLoader(userId));

    return registry;
  }

  /**
   * Creates a DataLoader for fetching balances.
   *
   * <p>This method creates a DataLoader that collects a set of bank account IDs and provides
   * the set to the balance service, which executes a batch request to fetch the balances.
   *
   * @param userId the user ID for which to create the DataLoader
   * @return a DataLoader for fetching balances
   */
  private DataLoader<UUID, BigDecimal> createBalanceDataLoader(String userId) {

    return DataLoader.newMappedDataLoader(
        (Set<UUID> bankAccountIds, BatchLoaderEnvironment environment) ->
            CompletableFuture.supplyAsync(
                () -> balanceService.getBalanceFor((Map) environment.getKeyContexts(), userId),
                balanceThreadPool));
  }
}
