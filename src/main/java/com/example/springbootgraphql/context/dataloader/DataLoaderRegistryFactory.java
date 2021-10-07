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
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {

  private final BalanceService balanceService;
  public static final String BALANCE_DATA_LOADER = "BALANCE_DATA_LOADER";
  private static final Executor balanceThreadPool =
      CorrelationIdPropagationExecutor.wrap(
          Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

  public DataLoaderRegistry create(String userId) {
    var registry = new DataLoaderRegistry();

    registry.register(BALANCE_DATA_LOADER, createBalanceDataLoader(userId));

    return registry;
  }

  /**
   * @param userId
   * @return DataLoader that collections a set of {@code bankAccountIds} and provides the set into
   *     the {@code getBalanceFor()} service, which executes a batch request.
   */
  //  private DataLoader<UUID, BigDecimal> createBalanceDataLoader(String userId) {
  //
  //    return DataLoader.newMappedDataLoader(
  //        (Set<UUID> bankAccountIds, BatchLoaderEnvironment environment) ->
  //            CompletableFuture.supplyAsync(
  //                () -> balanceService.getBalanceFor(bankAccountIds, userId), balanceThreadPool),
  //        DataLoaderOptions.newOptions()
  //            .setBatchLoaderContextProvider(() -> "This is the context From Data Loader"));
  //  }

  private DataLoader<UUID, BigDecimal> createBalanceDataLoader(String userId) {

    return DataLoader.newMappedDataLoader(
        (Set<UUID> bankAccountIds, BatchLoaderEnvironment environment) ->
            CompletableFuture.supplyAsync(
                () -> balanceService.getBalanceFor((Map) environment.getKeyContexts(), userId),
                balanceThreadPool),
        DataLoaderOptions.newOptions()
            .setBatchLoaderContextProvider(() -> "This is the context from Data Loader"));
  }
}
