package com.example.springbootgraphql.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScalarConfig {

  // Alternatively you can implement your own by extending the GraphQLScalarType
  // and implement at Corecing interface
  @Bean
  public GraphQLScalarType nonNegativeInt() {
    return ExtendedScalars.NonNegativeInt;
  }
}
