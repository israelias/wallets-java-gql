package com.example.springbootgraphql.publisher;

import com.example.springbootgraphql.domain.bank.BankAccount;
import graphql.schema.diff.reporting.DifferenceReporter;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Slf4j
@Component
public class BankAccountPublisher {
  private final FluxProcessor<BankAccount, BankAccount> processor;
  private final FluxSink<BankAccount> sink;

  public BankAccountPublisher() {
    this.processor = DirectProcessor.<BankAccount>create().serialize();
    this.sink = processor.sink();
  }

  public void publish(BankAccount bankAccount) {
    sink.next(bankAccount);
  }

  public Publisher<BankAccount> getBankAccountPublisher() {
    return processor.map(
        bankAccount -> {
          log.info("Publishing bank account {}", bankAccount);
          return bankAccount;
        });
  }

  public Publisher<BankAccount> getBankAccountPublisherFor(UUID id) {
    return processor
        .filter(bankAccount -> id.equals(bankAccount.getId()))
        .map(
            bankAccount -> {
              log.info("Publishing individual subscription for bank account {}", bankAccount);
              return bankAccount;
            });
  }
}
