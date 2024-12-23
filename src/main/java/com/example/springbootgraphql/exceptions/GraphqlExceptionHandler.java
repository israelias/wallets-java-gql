package com.example.springbootgraphql.exceptions;

import graphql.GraphQLException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GraphqlExceptionHandler
 *
 * <p>Since exceptions are enabled, exceptions will be raised everywhere (schema errors, runtime
 * errors). So we need to cover our bases by catching those errors and defining runtime handlers
 * along with any/all GraphQLExceptionHandlers or GraphQLErrorHandlers. This ensures that not all
 * exceptions are invoked as they are in our control, or are only invoked within our control.
 */
@Component
public class GraphqlExceptionHandler {

  /**
   * Handles {@link GraphQLException} and {@link ConstraintViolationException}.
   *
   * @param e the exception to handle
   * @return a {@link ThrowableGraphQLError} representing the handled exception
   */
  @ExceptionHandler({GraphQLException.class, ConstraintViolationException.class})
  public ThrowableGraphQLError handle(Exception e) {
    return new ThrowableGraphQLError(e);
  }

  /**
   * Handles  {@link AccessDeniedException}.
   *
   * @param e the exception to handle
   * @return a {@link ThrowableGraphQLError} representing the handled exception with a {@code FORBIDDEN} status
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ThrowableGraphQLError handle(AccessDeniedException e) {
    return new ThrowableGraphQLError(e, HttpStatus.FORBIDDEN.getReasonPhrase());
  }

  /**
   * Handles {@link RuntimeException}.
   *
   * @param e the exception to handle
   * @return a {@link ThrowableGraphQLError} representing the handled exception with an {@code INTERNAL_SERVER_ERROR} status
   */
  @ExceptionHandler(RuntimeException.class)
  public ThrowableGraphQLError handle(RuntimeException e) {
    return new ThrowableGraphQLError(e, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
  }
}
