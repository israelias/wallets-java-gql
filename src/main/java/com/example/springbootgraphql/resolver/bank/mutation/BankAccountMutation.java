package com.example.springbootgraphql.resolver.bank.mutation;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Currency;
import com.example.springbootgraphql.domain.bank.input.CreateBankAccountInput;
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
 * BankAccountMutation
 *
 * <p>Specifying DataFetchingEnvironment as the last parameter in the resolver ensures that the
 * framework will automatically inject this in and give you access to it. If there are no
 * parameters, we can specify it as the only one on the resolver.
 *
 * <p>{@link com.example.springbootgraphql.resolver.bank.query.BankAccountQueryResolver
 * BankAccountQueryResolver} The Environment allows us access to key methods and functionalities
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

  private final Clock clock;

  public BankAccount createBankAccount(@Valid CreateBankAccountInput input) {
    log.info("Creating bank account for {}", input);

    return BankAccount.builder()
        .id(UUID.randomUUID())
        .currency(Currency.PHP)
        .createdAt(ZonedDateTime.now(clock))
        .createdOn(LocalDate.now(clock))
        .build();
  }
}
