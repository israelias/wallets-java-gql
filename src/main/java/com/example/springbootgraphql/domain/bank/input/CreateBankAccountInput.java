package com.example.springbootgraphql.domain.bank.input;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * CreateBankAccountInput
 *
 * <p>This class represents the input required to create a bank account.
 * It includes the first name and age of the account holder.
 *
 * <p>With two or more variables, we would annotate this as {@code @Value}. Otherwise, {@code @Data}. The use
 * of {@code @Value} will require additional Jackson annotation in order to deserialize it.
 */
@Data
public class CreateBankAccountInput {

  /**
   * The first name of the account holder.
   * This field is mandatory and cannot be blank.
   */
  @NotBlank String firstName;

  /**
   * The age of the account holder.
   */
  int age;
}
