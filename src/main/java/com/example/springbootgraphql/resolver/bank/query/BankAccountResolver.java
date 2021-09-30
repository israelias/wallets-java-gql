package com.example.springbootgraphql.resolver;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import com.example.springbootgraphql.domain.bank.Currency;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// GraphQLQueryResolver acts as a marker interface
// For framework to pick up all classes that implements
// the GQLQueryResolvery Marker Interface
// It then looks for a matching signature of
// the method to a GQL-defined query
// Here we have a matching bank account
// response, that takes in id with matching
// query name to the banktAccount query type

@Slf4j
@Component
public class BankAccountResolver implements GraphQLQueryResolver {

  public BankAccount bankAccount(UUID id) {
    log.info("Retrieving bank account id: {}", id);

    return BankAccount.builder()
        .id(id)
        .currency(Currency.PHP)
        .client(
            Client.builder().id(UUID.randomUUID()).firstName("Elias").lastName("Wrubel").build())
        .build();
  }
}
