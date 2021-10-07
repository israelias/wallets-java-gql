package com.example.springbootgraphql.resolver.bank.query;

import com.example.springbootgraphql.context.dataloader.DataLoaderRegistryFactory;
import com.example.springbootgraphql.domain.bank.BankAccount;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BankAccountResolver implements GraphQLResolver<BankAccount> {

  /**
   * BalanceResolver
   *
   * @param bankAccount
   * @param environment
   * @return dataLoader#load() Function that loads the bankAccount id to the dataLoader {@code
   *     bankAccount.getID()}
   */
  public CompletableFuture<BigDecimal> balance(
      BankAccount bankAccount, DataFetchingEnvironment environment) {

    log.info("Getting balance for {}", bankAccount.getId());

    DataLoader<UUID, BigDecimal> dataLoader =
        environment.getDataLoader(DataLoaderRegistryFactory.BALANCE_DATA_LOADER);
    return dataLoader.load(bankAccount.getId(), bankAccount);
  }
}
