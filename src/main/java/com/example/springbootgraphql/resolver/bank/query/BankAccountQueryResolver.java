package com.example.springbootgraphql.resolver.bank.query;

import com.example.springbootgraphql.repository.BankAccountRepository;
import com.example.springbootgraphql.connection.CursorUtil;
import com.example.springbootgraphql.context.CustomGraphQLContext;
import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Currency;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * <p>The {@code BankAccountQueryResolver} class is a Spring component that implements the {@link GraphQLQueryResolver} interface,
 * enabling it to handle GraphQL queries related to bank accounts -- to pick up all classes that
 * implements this type, which then looks for a matching signature of the method {@code
 * bankAccount(UUID id)} to the GraphQL-defined query {@code bankAccount(id: ID): BankAccount}.
 * This class is annotated with {@link Slf4j} for logging, {@link Component} to indicate that it is a Spring-managed bean,
 * and {@link RequiredArgsConstructor} to generate a constructor with required arguments.
 *
 * @see <a href="file:../../../../../../../../resources/graphql/query.graphqls">query.graphqls</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountQueryResolver implements GraphQLQueryResolver {

  /**
   * Dependency {@link BankAccountRepository} injected by {@link RequiredArgsConstructor}.
   */
  private final BankAccountRepository bankAccountRepository;

  /**
   * Dependency {@link CursorUtil} injected by {@link RequiredArgsConstructor}.
   */
  private final CursorUtil cursorUtil;
  /**
   * Dependency {@link Clock} injected by {@link RequiredArgsConstructor}.
   */
  private final Clock clock;

  /**
   * <p>The {@code bankAccount} method retrieves a {@link BankAccount} by its {@link UUID}.
   * It logs the retrieval process and the user ID from the custom {@link CustomGraphQLContext}.
   * It also logs the fields requested by the user in the query.
   *
   * @param id a {@link UUID} input
   * @param environment {@link DataFetchingEnvironment} as last param created once
   * @return a {@link BankAccount} object with the given UUID, current {@link Clock} time, and predefined {@link Currency}.
   */
  @PreAuthorize("hasAuthority('get:bank_account')")
  public BankAccount bankAccount(UUID id, DataFetchingEnvironment environment) {
    log.info("Retrieving bank account id: {}", id);

    CustomGraphQLContext context = environment.getContext();

    log.info("User ID: {}", context.getUserId());

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

    return BankAccount.builder()
        .id(id)
        .currency(Currency.PHP)
        .createdAt(ZonedDateTime.now(clock))
        .createdOn(LocalDate.now(clock))
        .build();
  }

  /**
   * <p>The {@code bankAccounts} method returns a paginated list of {@link BankAccount} instances.
   * It takes the number of items to return ({@code first}) and an optional {@code cursor} for pagination.
   * The method creates edges for each bank account and constructs a {@link DefaultConnection} object
   * with the edges and pagination information.
   *
   * @param first the number of items to return
   * @param cursor the cursor for pagination
   * @return a {@link Connection} of {@link BankAccount} instances.
   */
  public Connection<BankAccount> bankAccounts(int first, @Nullable String cursor) {

    List<Edge<BankAccount>> edges =
        getBankAccounts(cursor).stream()
            .map(
                bankAccount ->
                    new DefaultEdge<>(
                        bankAccount, cursorUtil.createCursorWith(bankAccount.getId())))
            .limit(first)
            .collect(Collectors.toUnmodifiableList());

    var pageInfo =
        new DefaultPageInfo(
            cursorUtil.getFirstCursorFrom(edges),
            cursorUtil.getLastCursorFrom(edges),
            cursor != null,
            edges.size() >= first);

    return new DefaultConnection<>(edges, pageInfo);
  }

  /**
   * <p>The {@code getBankAccounts} method returns a list of {@link BankAccount} instances.
   * If the {@code cursor} is null, it retrieves all bank accounts from the {@code bankAccountRepository}.
   * Otherwise, it retrieves the bank accounts via {@code bankAccountRepository} after the decoded {@code cursor}.
   *
   * @param cursor the cursor for pagination
   * @return a list of {@link BankAccount} instances
   */
  public List<BankAccount> getBankAccounts(String cursor) {
    if (cursor == null) {
      return bankAccountRepository.getBankAccounts();
    }
    return bankAccountRepository.getBankAccountsAfter(cursorUtil.decode(cursor));
  }
}
