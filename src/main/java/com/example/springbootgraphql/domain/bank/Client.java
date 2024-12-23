package com.example.springbootgraphql.domain.bank;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Setter;

/**
 * Represents a client in the banking domain.
 *
 * <p>This class is mutable and uses the Builder pattern for object creation.
 * It includes details such as the client ID, first name, middle names, and last name.
 */
@Setter
@Builder
public class Client {
  /**
   * The unique identifier of the client.
   */
  UUID id;

  /**
   * The first name of the client.
   */
  String firstName;

  /**
   * The middle names of the client.
   */
  List<String> middleNames;

  /**
   * The last name of the client.
   */
  String lastName;
}
