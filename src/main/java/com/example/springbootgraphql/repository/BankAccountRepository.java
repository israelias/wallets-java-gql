package com.example.springbootgraphql.repository;

import static java.util.UUID.fromString;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import com.example.springbootgraphql.domain.bank.Currency;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * InMemory Mock of BankAccountRepository.
 *
 * <p>The {@link BankAccountRepository} class is a Spring component that provides an in-memory implementation of a repository
 * for {@link BankAccount} objects. This class is annotated with {@link Component}, indicating that it is a Spring-managed bean.
 *
 * <p>This class provides an in-memory implementation of the BankAccountRepository.
 * It includes methods to retrieve bank accounts and bank accounts after a specific ID.
 */
@Component
public class BankAccountRepository {

  /**
   * <p>{@code bankAccounts} is a private final list of {@link BankAccount} objects,
   * which is initialized with a set of predefined bank accounts.
   * These accounts are sorted by their ID using a {@link Comparator} and collected into an unmodifiable list.
   *
   * <ul>TODO
   * <li>How to attach the node to the correlation_id? or to the actual client?
   * <li>or is this bit just a mock and for a different service
   * <li>or could this be where we attach MDC or balance or other service logic at node level
   * <li>or is this the block service on a different stack
   */
  private final List<BankAccount> bankAccounts =
      List.of(
              BankAccount.builder()
                  .id(fromString("c6aa269a-812b-49d5-b178-a739a1ed74cc"))
                  .currency(Currency.PHP)
                  .createdAt(ZonedDateTime.parse("2019-05-03T12:12:00+00:00"))
                  .build(),
              BankAccount.builder()
                  .id(fromString("410f5919-e50b-4790-aae3-65d2d4b21c77"))
                  .currency(Currency.CHF)
                  .createdAt(ZonedDateTime.parse("2020-12-03T10:15:30+00:00"))
                  .build(),
              BankAccount.builder()
                  .id(fromString("024bb503-5c0f-4d60-aa44-db19d87042f4"))
                  .currency(Currency.CHF)
                  .createdAt(ZonedDateTime.parse("2020-12-03T10:15:31+00:00"))
                  .build(),
              BankAccount.builder()
                  .id(fromString("48e4a484-af2c-4366-8cd4-25330597473f"))
                  .currency(Currency.USD)
                  .createdAt(ZonedDateTime.parse("2007-08-07T19:01:22+04:00"))
                  .build())
          .stream()
          .sorted(Comparator.comparing(BankAccount::getId))
          .collect(Collectors.toUnmodifiableList());

  /**
   * <p>The {@code getBankAccounts} method returns the list of bank accounts.
   * This method does not take any parameters and simply returns the {@code bankAccounts} list.
   * @return a list of {@link BankAccount} objects
   */
  public List<BankAccount> getBankAccounts() {
    return bankAccounts;
  }

  /**
   * <p>The {@code getBankAccountsAfter} method retrieves the list of bank accounts that come after a specific ID.
   * It takes a {@link UUID} as a parameter and uses a stream to filter and collect the bank accounts that have an ID greater than the provided ID
   * @param id the {@link UUID} to filter by
   * @return a list of {@link BankAccount} objects
   */
  public List<BankAccount> getBankAccountsAfter(UUID id) {
    return bankAccounts.stream()
        .dropWhile(bankAccount -> bankAccount.getId().compareTo(id) != 1)
        .collect(Collectors.toUnmodifiableList());
  }
}
