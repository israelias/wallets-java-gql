package com.example.springbootgraphql.exceptions;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import java.util.List;
import org.springframework.stereotype.Component;

// Disguise the exception messages if we're not happy with it
// or type of error set back
// or send the whole thing back

// @Component
// public class CustomGraphqlErrorHandler implements GraphQLErrorHandler {
//
//  //  @Override
//  public List<GraphQLError> processErrors(List<GraphQLError> errors) {
//    return null;
//  }
// }
