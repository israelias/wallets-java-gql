package com.example.springbootgraphql.resolver.bank.query;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import com.example.springbootgraphql.domain.bank.Currency;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import java.util.UUID;
import java.util.stream.Collectors;
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

  public BankAccount bankAccount(UUID id, DataFetchingEnvironment environment) {
    log.info("Retrieving bank account id: {}", id);

    // contains what fields the user has selected to include in query
    // environment.getSelectionSet();

    // or if we need to perform operations if user requests a and b vs a and c
    // environment.getSelectionSet().containsAnyOf(xxx)  | containsAllOf()

    // or perform an operation if one particular field is called:
    //      if (environment.getSelectionSet().contains('specialField')) {
    //          //do special stuff
    //      }

    var requestedFields =
        environment.getSelectionSet().getFields().stream()
            .map(SelectedField::getName)
            .collect(Collectors.toUnmodifiableSet());

    return BankAccount.builder().id(id).currency(Currency.PHP).build();
  }
}
