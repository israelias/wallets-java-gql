package com.example.springbootgraphql.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScalarConfig {

  /**
   *   This extends GraphQLScalarTypes directly from the schema library.
   *   Alternatively we could implement our own scalars by extending the
   *   GraphQLScalarType and implementing a Coercing interface.
   */

  @Bean
  public GraphQLScalarType nonNegativeInt() {
    return ExtendedScalars.NonNegativeInt;
  }

  @Bean
  public GraphQLScalarType date() {
    return ExtendedScalars.Date;
  }

  @Bean
  public GraphQLScalarType dateTime() {
    return ExtendedScalars.DateTime;
  }
}
