package com.example.springbootgraphql.exceptions;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CustomGraphqlErrorHandler
 *
 * @disabled This patten can allow us to disguise the exception messages if we're not happy with it,
 *     or the type of error it raises, by setting it ourselves before returning or sending the
 *     entire List of errors. In this example, we are overriding the GraphQLErrorHandler and
 *     returning a List of all errors
 * @see {@link com.example.springbootgraphql.exceptions.GraphqlExceptionHandler}
 */

// @Component
// public class CustomGraphqlErrorHandler implements GraphQLErrorHandler {
//  @Override
//  public List<GraphQLError> processErrors(List<GraphQLError> errors) {
//    return errors;
//  }
// }
