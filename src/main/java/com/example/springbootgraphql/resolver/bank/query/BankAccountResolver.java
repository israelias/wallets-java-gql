package com.example.springbootgraphql.resolver.bank.query;

import com.example.springbootgraphql.BankAccountRepository;
import com.example.springbootgraphql.connection.CursorUtil;
import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import com.example.springbootgraphql.domain.bank.Currency;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
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
@RequiredArgsConstructor
public class BankAccountResolver implements GraphQLQueryResolver {

  /** Inject bankAccountRepository */
  private final BankAccountRepository bankAccountRepository;

  private final CursorUtil cursorUtil;

  /**
   * bankAccount
   *
   * <p>Method that takes in {@code id} with a response {@link BankAccount} and query name {@code
   * bankAccount} that both match the `BankAccount` Java class and the `bankAccount` GraphQL-defined
   * query
   *
   * @param id a {@link UUID} input
   * @param environment {@link DataFetchingEnvironment} as last param
   * @return a {@link BankAccount} instance
   */
  public BankAccount bankAccount(UUID id, DataFetchingEnvironment environment) {
    log.info("Retrieving bank account id: {}", id);

    /**
     * requestedFields
     *
     * <p>DataFetchingEnvironment
     *
     * <dl>
     *   <dt>contains what fields the user has selected to include in query
     *   <dd>{@code environment.getSelectionSet();}
     *   <dt>or if we need to perform operations if user requests a&b | a&c | a
     *   <dd>{@code environment.getSelectionSet().containsAnyOf(a&b) | containsAllOf(a)}
     *   <dt>or perform an operation if one particular field is called
     *   <dd>{@code if (environment.getSelectionSet().contains('specialField')) //do special stuff }
     *       </dl
     */
    var requestedFields =
        environment.getSelectionSet().getFields().stream()
            .map(SelectedField::getName)
            .collect(Collectors.toUnmodifiableSet());

    log.info("Requested Fields: {}", requestedFields);

    return BankAccount.builder().id(id).currency(Currency.PHP).build();
  }

  public Connection<BankAccount> bankAccounts(int first, @Nullable String cursor) {

    List<Edge<BankAccount>> edges =
        bankAccountRepository.getBankAccounts().stream()
            .map(
                bankAccount -> new DefaultEdge<>(bankAccount, cursorUtil.from(bankAccount.getId())))
            .collect(Collectors.toUnmodifiableList());

    var firstCursor = cursorUtil.getFirstCursorFrom(edges);

    var lastCursor = cursorUtil.getLastCursorFrom(edges);

    var pageInfo =
        new DefaultPageInfo(firstCursor, lastCursor, cursor != null, edges.size() >= first);

    return new DefaultConnection<>(edges, pageInfo);
  }
}
