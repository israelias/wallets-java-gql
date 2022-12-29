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
   * Provides the hook to build a custom GraphQL Context wrapper.
   *
   * <p>{@link GraphQLContext} is the object made available by the `DataFetchingEnvironment`'s
   * {@code getContext()} method.
   *
   * @param httpServletRequest
   * @param httpServletResponse
   * @return
   */
  @Override
  public GraphQLContext build(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

    var userId = httpServletRequest.getHeader("user_id");

    var context =
        DefaultGraphQLServletContext.createServletContext()
            .with(httpServletRequest)
            .with(httpServletResponse)
            .with(dataLoaderRegistryFactory.create(userId))
            .build();

    return new CustomGraphQLContext(userId, context);
  }

  @Override
  public GraphQLContext build(Session session, HandshakeRequest handshakeRequest) {
    /** Currently not supporting web sockets so throw exception */
    //    throw new IllegalStateException("Unsupported.");
    return DefaultGraphQLWebSocketContext.createWebSocketContext()
        .with(session)
        .with(handshakeRequest)
        .build();
  }

  @Override
  public GraphQLContext build() {
    /** Currently not supporting a custom context build so throw exception */
    throw new IllegalStateException("Unsupported.");
  }
}
