# Spring Boot GraphQL Wallets App

Full stack CRUD app that stores personal asset references in a secure wallet
provider (`BankAccount`)
built with Java [Spring Boot](https://spring.io/projects/spring-boot)
and [GraphQL Java](https://www.graphql-java.com/) bootstrapped
with [GraphQL Java Kickstart](https://github.com/graphql-java-kickstart) managed, bundled, tested
via [Apache Maven](https://maven.apache.org/).

## Dependencies and Plugins

### Dependencies

- [graphql-java-kickstart]()
    - [graphql-spring-boot-starter](): GraphQL and GraphiQL Spring Framework Boot starter with
      Maven, Java Tools, Annotations, configs
    - [playground-spring-boot-starter](https://github.com/graphql-java-kickstart/graphql-spring-boot#enable-graphql-playground):
      GraphQL Playground access at root `/playground` which embeds a context-aware GraphQL IDE (
      GraphQL Playground React) for development workflows
    - [voyager-spring-boot-starter](https://github.com/graphql-java-kickstart/graphql-spring-boot#enable-graphql-voyager):
      GraphQL Voyager access at root `/voyager` which Represent any GraphQL API as an interactive
      graph.
- [graphql-java]()
    - [graphql-java-extended-scalars]()
    - [graphql-java-extended-validation]()
- [reactor-core](https://projectreactor.io/): reactive library for building non-blocking
  applications on the JVM
- [hibernate-validator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/):
  metadata model API from presentation to persistence layer for entity and method validation
- [spring-boot-starter-security](https://spring.io/projects/spring-security): authentication and
  authorization framework for Java apps with Servlet API integration
- [lombok](https://projectlombok.org/) Java annotation library that automatically plugs into a
  remote editor and build tools
- [junit](https://junit.org/junit5/docs/current/user-guide/): platform that serves as foundation for
  launching testing frameworks on the JVM configured to your local IDE (IntelliJ IDEA)
    - [junit-jupiter](https://junit.org/junit5/docs/current/user-guide/): combination of the new
      programming model and extension model for writing tests and extensions
      in [JUnit 5](https://junit.org/junit5/)
    - [junit-jupiter-api](http://junit.sourceforge.net/javadoc/): Provides classes used to describe,
      collect, run and analyze multiple tests, writing new tests and extensions.
    - [junit-jupiter-engine](): JUnit Jupiter test engine implementation, only required at runtime.
- [assertj-core](https://github.com/assertj/assertj-core): rich and intuitive set of strongly-typed
  assertions to use for unit testing with JUnit

### Plugins

- [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/):
  provides Spring Boot support in Apache Maven including packaging executable `jar` or `war`
  archives, running Spring Boot applications, generating build information and starting your Spring
  Boot application prior to running integration tests.
- [maven-resources-plugin](https://maven.apache.org/plugins/maven-resources-plugin/): Resources
  Plugin handles the copying of project resources to the output directory as disparate sets of
  resources: main resources and test resources, where main resources are the resources associated to
  the main source code and the test resources are associated to the test source code
- [maven-compiler-plugin](https://maven.apache.org/plugins/maven-compiler-plugin/): Compiler used to
  compile the sources of the project
- [mojohaus build-helper-maven-plugin](https://www.mojohaus.org/build-helper-maven-plugin/usage.html):
  Allows additional source directories to a Maven project since `pom.xml` only allows one source
  directory.
- [maven-surefire-plugin](https://maven.apache.org/surefire/maven-surefire-plugin/): designed to run
  unit tests
- [maven-failsafe-plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/): designed to run
  integration tests
- [maven-pmd-plugin](http://maven.apache.org/plugins/maven-pmd-plugin/): automatically run the PMD
  code analysis tool on your project's source code and generate a site report with its results

## Notes

### Async Resolvers

You can make graphql resolvers execute asynchronously by setting the return type to a
`CompletableFuture` and executing the logic within another thread. This of course does not work if
you do your logic as normal then return a `CompletableFuture.completedFuture(u)`. As here the logic
processing will occur in the graphql user thread.

Async resolvers permit the graphql server to execute multiple resolver methods in parallel. For
resolvers to execute in parallel, they must not depend on each other (parent/child fields).

Executing in parallel is useful if the client requests multiple fields that do not depend on each
other and could take a considerable amount of time to fetch. We now have response times of
worst-of (resolver A latency, resolver B latency) instead of resolver A latency + resolver B
latency.

Take time to size and shape your `threadpools`, keep an eye on them over time.

Also, be aware that when you execute resolvers async, then the child resolvers will execute in it's
parents async thread (See below: standard `CompletableFuture` chain).

```graphql
a {
# not async - tomcat user thread UT1
b {
# async - Thread 1 - Pool 1
c {
# not async - executed in Thread 1 - Pool 1
}
}
d {
# not async - tomcat user thread UT1
}
}
```

### Optimization

- [SelectionSet](https://www.graphql-java.com/documentation/v11/data-fetching/)
- [Field selection](https://www.graphql-java.com/documentation/v12/fieldselection/)
- [DataFetchingFieldSelectionSet](https://github.com/graphql-java/graphql-java/blob/master/src/main/java/graphql/schema/DataFetchingFieldSelectionSet.java)

A very powerful feature of graphql spring boot is that you have access to the query's selection set.
A selection set is the set of fields the user requested.

Having direct access to the fields can enable you to make performance optimized queries requesting
only the needed data. For example an SQL backed system may be able to use the field sub selection to
only retrieve the columns that have been asked for.

If you look ahead in the selection set, you may be able to optimize further and collapse two backend
resource calls into one. For example, if you can retrieve the sellingLocations data within the
products API call. You can group everything into one API query instead of two.

```graphql
query {
    products {
        # the fields below represent the selection set
        name
        description
        sellingLocations {
            state
        }
    }
}
```

The selection set `DataFetchingFieldSelectionSet` contains many useful utility methods such as:
`contains`, `containsAnyOf`, `containsAnyOf`. These can be used as the predicate to make your API
call selection.

To get the requested field names you can stream the fields, filter and collect into a set.

### Scalars

- [Extended Scalars](https://github.com/graphql-java/graphql-java-extended-scalars)

### Validation

There are two ways of validating your graphql input types, via `Directives` or manually validating
the input type in your resolver. The following shows both the more old-school approach which puts
the
`JSR-303` bean validation annotations on our `POJO`s; along with a `Schema Directive` approach:

```java

@Slf4j
@Validated
@Component
@RequiredArgsConstructor
public class BankAccountMutation implements GraphQLMutationResolver {

  private final Clock clock;

  /**
   * JSR-303 Bean Validation 
   */
  public BankAccount createBankAccount(@Valid CreateBankAccountInput input) {
    log.info("Creating bank account for {}", input);
    return getBankAccount(UUID.randomUUID());
  }

  /**
   * Schema Directive Validation 
   */
  public BankAccount updateBankAccount(UUID id, String name, int age) {
    log.info("Updating bank account for {}. Name: {}, age: {}", id, name, age);
    return getBankAccount(id);
  }

}
```

The Directive version implements `graphql-java-extended-validation` which allow us to remove all
java bean validation annotations, and instead place the annotations directly on the schema.

### Port 8080 is in use

- [Kill 8080](https://stackoverflow.com/questions/40118878/8080-port-already-taken-issue-when-trying-to-redeploy-project-from-spring-tool-s)

```bash
sudo lsof -i tcp:8181

kill -9 PID
```

## Spring Initializer Disclaimers

The following was discovered as part of building this project:

* The original package name 'com.example.springboot-graphql' is invalid and this project uses '
  com.example.springbootgraphql' instead.

## Code

Patterns in this repo, including schema types, resolvers, Java classes, GraphQL query techniques for
general RDBS performance, and opinionated settings for clean code are modeled
after [Philip Starritt's GraphQL Java series](https://www.youtube.com/c/PhilipStarritt/videos)

Please refer to Philip's [starter-pack](https://github.com/philip-jvm/learn-spring-boot-graphql)

## Resources

### Spring Boot Dry

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.5/maven-plugin/reference/html/#build-image)

### IntelliJ IDEA

- [IntelliJ Schemas and DTDs](https://www.jetbrains.com/help/idea/settings-languages-schemas-and-dtds.html)
- [IntelliJ Extenal Docs Access](https://www.jetbrains.com/help/idea/sdk.html#access-external-documentation)
- [IntelliJ Shared Indexes](https://www.jetbrains.com/help/idea/shared-indexes.html#project-shared-indexes)

### PMD on Maven

- [Maven PMD Plugin Default](https://github.com/apache/maven-pmd-plugin/blob/master/src/main/resources/rulesets/java/maven-pmd-plugin-default.xml)
- [Spring Initializer Docs](https://docs.spring.io/initializr/docs/0.4.x/reference/htmlsingle/)
- [PMD Rule Configuration](https://pmd.github.io/latest/pmd_userdocs_configuring_rules.html)
- [PMD Making Rulesets](https://pmd.github.io/latest/pmd_userdocs_making_rulesets.html)

### Repo Benchmark

- [Philip Starritt Spring Boot GraphQL Tutorial](https://www.youtube.com/c/PhilipStarritt/videos)