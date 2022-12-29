package com.example.springbootgraphql.resolver.bank.subscription;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.publisher.BankAccountPublisher;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountSubscription implements GraphQLSubscriptionResolver {

  private final BankAccountPublisher publisher;

  public Publisher<BankAccount> bankAccounts(DataFetchingEnvironment environment) {
    /** environment is how we pass the session to context * */
    return publisher.getBankAccountPublisher();
  }

  public Publisher<BankAccount> bankAccount(UUID id) {
    return publisher.getBankAccountPublisherFor(id);
  }
}
