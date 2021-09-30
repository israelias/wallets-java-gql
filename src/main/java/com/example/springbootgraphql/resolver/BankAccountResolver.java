package com.example.springbootgraphql.resolver;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Currency;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BankAccountResolver implements GraphQLQueryResolver {
  public BankAccount bankAccount(UUID id) {
    log.info("Retrieving bank account id: {}", id);
    return BankAccount.builder().id(id).currency(Currency.PHP).name("Elias").build();
  }
}
