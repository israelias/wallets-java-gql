package com.example.springbootgraphql.context;

import graphql.kickstart.servlet.context.GraphQLServletContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoaderRegistry;

/**
 * CustomGraphQLContext implements {@link GraphQLServletContext}
 *
 * <p>Customized returns of delegate methods to grab from the context. In order to use the built-in
 * GraphQL default servlet context
 */
@Getter
@RequiredArgsConstructor
public class CustomGraphQLContext implements GraphQLServletContext {

  private final String userId;
  private final GraphQLServletContext context;

  @Override
  public List<Part> getFileParts() {
    return context.getFileParts();
  }

  @Override
  public Map<String, List<Part>> getParts() {
    return context.getParts();
  }

  @Override
  public HttpServletRequest getHttpServletRequest() {
    return context.getHttpServletRequest();
  }

  @Override
  public HttpServletResponse getHttpServletResponse() {
    return context.getHttpServletResponse();
  }

  /** @return the subject to execute the query as. */
  @Override
  public Optional<Subject> getSubject() {
    return context.getSubject();
  }

  /** @return the DataLoader registry to use for the execution. Must not return <code>null</code> */
  @Override
  public @NonNull DataLoaderRegistry getDataLoaderRegistry() {
    return context.getDataLoaderRegistry();
  }
}
