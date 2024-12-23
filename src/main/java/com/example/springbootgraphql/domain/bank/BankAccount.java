package com.example.springbootgraphql.domain.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Represents a bank account.
 *
 * <p>This class is immutable and uses the Builder pattern for object creation.
 * It includes details such as the account ID, client, currency, assets, creation date, and balance.
 */
@Builder
@Value
public class BankAccount {
  /**
   * The unique identifier of the bank account.
   */
  UUID id;

  /**
   * The client associated with the bank account.
   */
  Client client;

  /**
   * The currency of the bank account.
   */
  Currency currency;

  /**
   * The assets associated with the bank account.
   */
  Asset assets;

  /**
   * The date and time when the bank account was created.
   */
  ZonedDateTime createdAt;

  /**
   * The date when the bank account was created.
   */
  LocalDate createdOn;

  /**
   * The balance of the bank account.
   */
  BigDecimal balance;
}
