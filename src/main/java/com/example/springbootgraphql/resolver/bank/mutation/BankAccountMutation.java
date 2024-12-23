package com.example.springbootgraphql.resolver.bank.mutation;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Currency;
import com.example.springbootgraphql.domain.bank.input.CreateBankAccountInput;
import com.example.springbootgraphql.publisher.BankAccountPublisher;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * BankAccountMutation: Resolver for the BankAccount type in the GraphQL schema.
 *
 * <p>The {@link BankAccountMutation} class is a Spring component that implements the {@link GraphQLMutationResolver} interface,
 * allowing it to handle GraphQL mutations related the {@link BankAccount} type in the GraphQL schema.
 * This class is annotated with {@link Slf4j} for logging, {@link Component} to indicate that it is a Spring-managed bean,
 * {@link RequiredArgsConstructor} to generate a constructor with required arguments, and {@link Validated} to enable validation.
 * Overall, the {@link BankAccountMutation} class provides methods to create and update bank accounts, with logging and event publishing capabilities.
 *
 * <p>Notes:
 * <p>Specifying DataFetchingEnvironment as the last parameter in the resolver ensures that the
 * framework will automatically inject this in and give you access to it. If there are no
 * parameters, we can specify it as the only one on the resolver.
 *
 * <p>{@link com.example.springbootgraphql.resolver.bank.query.BankAccountQueryResolver
 * BankAccountQueryResolver} The Environment allows access to key methods and functionalities
 * such as
 *
 * <ul>
 *   <li>fields (All fields selected by the user)
 *   <li>selectionset (You could transform it into your sql query directory to optimize performace)
 *   <li>context (created once on the query, will always stay the same and will be propagated
 *       through all the resolvers, useful for auth data.
 *   <li>variables
 *   <li>args
 *   <li>dataLoaders (which solve the n+1 problem See <a
 *       href="https://medium.com/the-marcy-lab-school/what-is-the-n-1-problem-in-graphql-dd4921cb3c1a">
 *       Medium</a>
 * </ul>
 *
 * @see <a
 *     href="https://javadoc.io/doc/com.graphql-java/graphql-java/12.0/graphql/schema/DataFetchingEnvironment.html">
 *     java-doc DataFetchingEnvironment</a>
 * @see <a
 *     href="https://github.com/graphql-java/graphql-java/blob/master/src/main/java/graphql/schema/DataFetchingEnvironment.java">
 *     graphql-java GitHub DataFetchingEnvironment</a>
 * @see <a href="https://www.graphql-java.com/documentation/v11/data-fetching/">graphql-java
 *     DataFetching</a>
 * @author Joem Elias Sanez
 * @since 2021-10-02
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class BankAccountMutation implements GraphQLMutationResolver {

  /**
   * The {@code clock} field is a {@link Clock} instance that provides access to the current date and time.
   * It is injected via {@link RequiredArgsConstructor}.
   */
  private final Clock clock;
  /**
   * The {@code bankAccountPublisher} field is a {@link BankAccountPublisher} instance that provides the ability to publish bank account events.
   * It is injected via {@link RequiredArgsConstructor}.
   */
  private final BankAccountPublisher bankAccountPublisher;

  /**
   * <p>The {@code createBankAccount} method creates a new bank account using the provided {@link CreateBankAccountInput}.
   * It logs the creation process and calls the private {@link BankAccountMutation#getBankAccount} method to generate a new
   * {@link BankAccount} object with a random UUID:
   *
   * @param input the input data instance of {@link CreateBankAccountInput} for creating a bank account
   * @return the created bank account
   */
  public BankAccount createBankAccount(@Valid CreateBankAccountInput input) {
    log.info("Creating bank account for {}", input);
    return getBankAccount(UUID.randomUUID());
  }

  /**
   * <p>The {@code updateBankAccount} method updates an existing bank account identified by its {@link UUID}.
   * It logs the update process and calls the {@link BankAccountMutation#getBankAccount(UUID)} method to retrieve the updated {@link BankAccount} object.
   *
   * <p>Notes: Schema Directive Validation (Chapter 32)
   *
   * @param id the {@link UUID} of the bank account to update
   * @param name the new {@link String} name for the bank account
   * @param age the new {@code int} age for the bank account
   * @return the updated bank account
   */
  public BankAccount updateBankAccount(UUID id, String name, int age) {
    log.info("Updating bank account for {}. Name: {}, age: {}", id, name, age);
    return getBankAccount(id);
  }

  /**
   * The private {@code getBankAccount} method generates a {@link BankAccount} object with the given {@link UUID}, current {@link BankAccountMutation#clock}, and predefined {@link Currency}.
   * It then publishes the bank account event using the {@link BankAccountPublisher#publish(BankAccount)} method.
   *
   * @param id the {@link UUID} of the bank account to retrieve
   * @return the retrieved bank account
   */
  private BankAccount getBankAccount(UUID id) {
    var bankAccount =
        BankAccount.builder()
            .id(id)
            .currency(Currency.PHP)
            .createdAt(ZonedDateTime.now(clock))
            .createdOn(LocalDate.now(clock))
            .build();

    /** Subscription (Chapter 33) */
    bankAccountPublisher.publish(bankAccount);

    return bankAccount;
    //    return BankAccount.builder()
    //        .id(UUID.randomUUID())
    //        .currency(Currency.PHP)
    //        .createdAt(ZonedDateTime.now(clock))
    //        .createdOn(LocalDate.now(clock))
    //        .build();
  }
}
