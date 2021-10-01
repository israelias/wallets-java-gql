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

/**
 * BankAccountResolver
 *
 * <p>GraphQLQueryResolver acts as a marker interface For framework to pick up all classes that
 * implements the GQLQueryResolver Marker Interface It then looks for a matching signature of the
 * method to a GQL-defined query Here we have a matching bank account response, that takes in id
 * with matching query name to the bankAccount query type
 */
@Slf4j
@Component
public class BankAccountResolver implements GraphQLQueryResolver {

  public BankAccount bankAccount(UUID id, DataFetchingEnvironment environment) {
    log.info("Retrieving bank account id: {}", id);

    /**
     * Options
     *
     * <dl>
     *   <dt>contains what fields the user has selected to include in query
     *   <dd>{@code environment.getSelectionSet();}
     *   <dt>or if we need to perform operations if user requests a&b | a&c | a
     *   <dd>{@code environment.getSelectionSet().containsAnyOf(a&b) | containsAllOf(a)}
     *   <dt>or perform an operation if one particular field is called
     *   <dd>{@code
     *          if (environment.getSelectionSet().contains('specialField')) {
     *            //do special stuff
     *          }
     *       }
     *       </dl
     */


    var requestedFields =
        environment.getSelectionSet().getFields().stream()
            .map(SelectedField::getName)
            .collect(Collectors.toUnmodifiableSet());

    return BankAccount.builder().id(id).currency(Currency.PHP).build();
  }
}
