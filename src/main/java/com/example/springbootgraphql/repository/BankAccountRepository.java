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

/** InMemory Mock of BankAccountRepository */
@Component
public class BankAccountRepository {

  // TODO How to attach the node to the correlation_id? or to the actual client?
  // Or is this bit just a mock and for a different service
  // or could this be where we attach MDC or balance or other service logic at node level
  // Or is this the block service on a different stack
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

  public List<BankAccount> getBankAccounts() {
    return bankAccounts;
  }

  public List<BankAccount> getBankAccountsAfter(UUID id) {
    return bankAccounts.stream()
        .dropWhile(bankAccount -> bankAccount.getId().compareTo(id) != 1)
        .collect(Collectors.toUnmodifiableList());
  }
}
