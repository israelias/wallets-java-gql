package com.example.springbootgraphql.connection;

import com.example.springbootgraphql.domain.bank.BankAccount;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CursorUtil {

  public ConnectionCursor from(UUID id) {
    return new DefaultConnectionCursor(
        Base64.getEncoder().encodeToString(id.toString().getBytes(StandardCharsets.UTF_8)));
  }

  public <T> ConnectionCursor getFirstCursorFrom(List<Edge<T>> edges) {
    return edges.isEmpty() ? null : edges.get(0).getCursor();
  }

  public <T> ConnectionCursor getLastCursorFrom(List<Edge<T>> edges) {
    return edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
  }
}
