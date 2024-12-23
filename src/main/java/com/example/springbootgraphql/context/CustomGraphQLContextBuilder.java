package com.example.springbootgraphql.context;

import com.example.springbootgraphql.context.dataloader.DataLoaderRegistryFactory;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CustomGraphQLContextBuilder implements {@link GraphQLServletContextBuilder}, an extension of
 * `GraphQLContextBuilder`, to return a {@link DefaultGraphQLServletContext} environment with the
 * values of a {@code userID} and a custom object context: {@code CustomGraphQLContext}
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

  private final DataLoaderRegistryFactory dataLoaderRegistryFactory;

  /**
   * Builds a custom GraphQL context for HTTP requests.
   *
   * <p>This method constructs a {@link DefaultGraphQLServletContext} with the provided
   * {@link HttpServletRequest} and {@link HttpServletResponse}, and includes a DataLoaderRegistry
   * created using the user ID extracted from the request header. The resulting context is wrapped
   * in a {@link CustomGraphQLContext} which includes the user ID.
   *
   * @param httpServletRequest the HTTP servlet request
   * @param httpServletResponse the HTTP servlet response
   * @return a custom GraphQL context containing the user ID and the servlet context
   */
  @Override
  public GraphQLContext build(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    /* Extract the user ID from the request header */
    var userId = httpServletRequest.getHeader("user_id");

    /* Create the default GraphQL servlet context with the request, response, and DataLoaderRegistry */
    var context =
        DefaultGraphQLServletContext.createServletContext()
            .with(httpServletRequest)
            .with(httpServletResponse)
            .with(dataLoaderRegistryFactory.create(userId))
            .build();

    /* Return a new custom GraphQL context with the user ID and the created context */
    return new CustomGraphQLContext(userId, context);
  }

  /**
   * Builds a custom GraphQL context for WebSocket sessions.
   *
   * <p>This method constructs a {@link DefaultGraphQLWebSocketContext} with the provided
   * {@link Session} and {@link HandshakeRequest}. Currently, it does not support custom WebSocket
   * contexts and returns the default context.
   *
   * @param session the WebSocket session
   * @param handshakeRequest the WebSocket handshake request
   * @return a default GraphQL WebSocket context
   */
  @Override
  public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
    /* Currently not supporting web sockets so throw exception */
    //    throw new IllegalStateException("Unsupported.");
    return DefaultGraphQLWebSocketContext.createWebSocketContext()
        .with(session)
        .with(handshakeRequest)
        .build();
  }

  /**
   * Builds a custom GraphQL context without any parameters.
   *
   * <p>This method is not supported and throws an {@link IllegalStateException}.
   *
   * @return nothing, as this method always throws an exception
   * @throws IllegalStateException always thrown to indicate unsupported operation
   */
  @Override
  public GraphQLContext build() {
    /* Currently not supporting a custom context build so throw exception */
    throw new IllegalStateException("Unsupported.");
  }
}
