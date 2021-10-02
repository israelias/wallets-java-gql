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
 * BankAccountResolver implements {@link GraphQLQueryResolver} to pick up all classes that
 * implements this type, which then looks for a matching signature of the method {@code
 * bankAccount(UUID id)} to the GraphQL-defined query {@code bankAccount(id: ID): BankAccount}.
 *
 * @see <a href="file:../../../../../../../../resources/graphql/query.graphqls">query.graphqls</a>
 */
@Slf4j
@Component
public class BankAccountResolver implements GraphQLQueryResolver {

  /**
   * bankAccount
   * <p>Method that takes in {@code id} with a response {@link BankAccount} and query
   * name {@code bankAccount} that both match the `BankAccount` Java class and the `bankAccount` GraphQL-defined query
   *
   * @param id a {@link UUID} input
   * @param environment {@link DataFetchingEnvironment} as last param
   * @return a {@link BankAccount} instance
   */
  public BankAccount bankAccount(UUID id, DataFetchingEnvironment environment) {
    log.info("Retrieving bank account id: {}", id);

    /**
     * requestedFields
     * <p>DataFetchingEnvironment
     *
     * <dl>
     *   <dt>contains what fields the user has selected to include in query
     *   <dd>{@code environment.getSelectionSet();}
     *   <dt>or if we need to perform operations if user requests a&b | a&c | a
     *   <dd>{@code environment.getSelectionSet().containsAnyOf(a&b) | containsAllOf(a)}
     *   <dt>or perform an operation if one particular field is called
     *   <dd>{@code if (environment.getSelectionSet().contains('specialField'))  //do special stuff
     *        } </dl
     */
    var requestedFields =
        environment.getSelectionSet().getFields().stream()
            .map(SelectedField::getName)
            .collect(Collectors.toUnmodifiableSet());

    return BankAccount.builder().id(id).currency(Currency.PHP).build();
  }
}
