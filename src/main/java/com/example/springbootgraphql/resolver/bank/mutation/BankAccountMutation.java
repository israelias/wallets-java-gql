package com.example.springbootgraphql.resolver.bank.mutation;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Currency;
import com.example.springbootgraphql.domain.bank.input.CreateBankAccountInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Specifying DataFetchinigEnvironment as the last parameter in the resolver
// ensures that the framework will automatically inject this in and give you
// access to it. If there are no parameters, we can specify it as the only
// on the resolver. The Environment allows us access to key methods, and functionalities
// such as fields, (selectionset). All fields selected by the user. You could transform
// it into your sql query directory to optimize performace.
// Context access: created once on the query, will always stay the same and will be
// propagated through all the resolvers ( for auth data ).
// Variables:
// Args:
// Dataloaders: (solve n + 1 problem)
// https://javadoc.io/doc/com.graphql-java/graphql-java/12.0/graphql/schema/DataFetchingEnvironment.html
// https://github.com/graphql-java/graphql-java/blob/master/src/main/java/graphql/schema/DataFetchingEnvironment.java
// https://www.graphql-java.com/documentation/v11/data-fetching/

@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountMutation implements GraphQLMutationResolver {

  private final Clock clock;

  public BankAccount createBankAccount(CreateBankAccountInput input) {
    log.info("Creating bank account for {}", input);

    return BankAccount.builder()
        .id(UUID.randomUUID())
        .currency(Currency.PHP)
        .createdAt(ZonedDateTime.now(clock))
        .createdOn(LocalDate.now(clock))
        .build();
  }
}
