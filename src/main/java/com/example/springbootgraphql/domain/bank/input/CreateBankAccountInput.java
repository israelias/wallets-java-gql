package com.example.springbootgraphql.domain.bank.input;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * CreateBankAccountInput
 *
 * <p>With two or more variables, we would annotate this as @Value Otherwise, @Data. The use
 * of @Value will require additional jackson annotation in order to deserialize it
 */
@Data
public class CreateBankAccountInput {

  @NotBlank String firstName;
  int age;
}
