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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Resolver for the BankAccount type in the GraphQL schema.
 *
 * <p>This resolver provides methods to fetch additional data for the BankAccount type, such as the balance.
 */
@Slf4j
@Component
public class BankAccountResolver implements GraphQLResolver<BankAccount> {

  /**
   * Fetches the balance for a given bank account.
   *
   * <p>This method uses a DataLoader to asynchronously fetch the balance for the specified bank account.
   * The method is secured with a pre-authorization check to ensure the user has the required authority.
   *
   * @param bankAccount the bank account for which to fetch the balance
   * @param environment the data fetching environment
   * @return a CompletableFuture containing the balance of the bank account
   */
  @PreAuthorize("hasAuthority('get:bank_account_balance')")
  public CompletableFuture<BigDecimal> balance(
      BankAccount bankAccount, DataFetchingEnvironment environment) {

    log.info("Getting balance for {}", bankAccount.getId());

    DataLoader<UUID, BigDecimal> dataLoader =
        environment.getDataLoader(DataLoaderRegistryFactory.BALANCE_DATA_LOADER);
    return dataLoader.load(bankAccount.getId(), bankAccount);
  }
}
