package com.example.springbootgraphql.exceptions;

import graphql.GraphQLException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Invoke correct exception handlers
// Since exceptions are enabled, we need to catch runtime handlers
// along with the custom graphql handler so that not all exceptions
// are invoked and/or they are all controlled

@Component
public class GraphqlExceptionHandler {

  @ExceptionHandler({GraphQLException.class, ConstraintViolationException.class})
  public ThrowableGraphQLError handle(Exception e) {
    return new ThrowableGraphQLError(e);
  }

  @ExceptionHandler(RuntimeException.class)
  public ThrowableGraphQLError handle(RuntimeException e) {
    return new ThrowableGraphQLError(e, "Internal Server Error");
  }
}
