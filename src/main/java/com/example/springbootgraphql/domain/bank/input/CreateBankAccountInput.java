package com.example.springbootgraphql.domain.bank.input;

import javax.validation.constraints.NotBlank;
import lombok.Data;

// Two or more variables = Value otherwise, Data
// Value will have to add extra jackson annotation to deserialize it

@Data
public class CreateBankAccountInput {

  @NotBlank
  String firstName;
  int age;
}
