package com.example.springbootgraphql.domain.bank.input;

import lombok.Data;

// Two or more variables = Value otherwise, Data
// Value will have to add extra jackson annotion to deserialize it

@Data
public class CreateBankAccountInput {
    String firstName;
}
