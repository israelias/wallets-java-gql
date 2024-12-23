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

/**
 * <p>The {@link BankAccountPublisher} class is a Spring component responsible for publishing bank account events using Reactor's {@link FluxProcessor}.
 * This class is annotated with {@link Slf4j} for logging and {@link Component} to indicate that it is a Spring-managed bean.
 * <p>Overall, the {@link BankAccountPublisher} class provides a way to publish and subscribe to bank account events, with logging for each event published.
 */
@Slf4j
@Component
public class BankAccountPublisher {
  private final FluxProcessor<BankAccount, BankAccount> processor;
  private final FluxSink<BankAccount> sink;

  /**
   * The {@code BankAccountPublisher} constructor initializes the class's two main fields: {@code processor} and {@code sink}.
   *
   * <p>The {@code processor} is a {@link FluxProcessor} that handles the stream of {@link BankAccount} events, and the {@code sink} is a {@link FluxSink}
   * that allows pushing events into the {@code processor}.
   */
  public BankAccountPublisher() {
    this.processor = DirectProcessor.<BankAccount>create().serialize();
    this.sink = processor.sink();
  }

  /**
   * <p>The {@code publish} method is used to publish a {@link BankAccount} event.
   * It takes a {@link BankAccount} object as a parameter and pushes it into the {@code sink} using the {@code next} method.
   *
   * @param bankAccount the {@link BankAccount} to publish
   */
  public void publish(BankAccount bankAccount) {
    sink.next(bankAccount);
  }

  /**
   * <p>The {@code getBankAccountPublisher} method returns a {@code Publisher} that emits all {@link BankAccount} events.
   * It logs each event before returning it.
   * @return a Publisher of {@link BankAccount}
   */
  public Publisher<BankAccount> getBankAccountPublisher() {
    return processor.map(
        bankAccount -> {
          log.info("Publishing bank account {}", bankAccount);
          return bankAccount;
        });
  }

  /**
   * <p>The {@code getBankAccountPublisherFor} method returns a {@code Publisher} that emits events for a specific bank account identified by its {@link UUID}.
   * It filters the events by the given {@link UUID} and logs each event before returning it.
   *
   * @param id the {@link UUID} of the bank account to filter by
   * @return a Publisher of {@link BankAccount}
   */
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
