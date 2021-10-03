# Spring Boot GraphQL Wallets App

Full stack CRUD app that stores personal asset references in a secure wallet provider (`BankAccount`)
built with Java [Spring Boot](https://spring.io/projects/spring-boot)
and [GraphQL Java](https://www.graphql-java.com/) bootstrapped
with [GraphQL Java Kickstart](https://github.com/graphql-java-kickstart) managed, bundled, tested
via [Apache Maven](https://maven.apache.org/).

## Available Scripts

### Maven Wrapper

The project uses [Takari Maven plugin](https://github.com/takari/takari-maven-plugin) to upstream to the Apache Maven
project via [Maven Wrapper](https://github.com/takari/maven-wrapper) to make auto installation in a simple Spring Boot
project.

Note: we use the executable `mvnw` in place of `mvn`, which stands now as the Maven command line program.
See [maven-plugin](https://docs.spring.io/spring-boot/docs/2.5.5/maven-plugin/reference/htmlsingle/#?) (Docs).
See [repackage-example-exclude-dependancy](https://docs.spring.io/spring-boot/docs/2.3.x/maven-plugin/reference/html/#repackage-example-exclude-dependency)

- #### `./mvnw spring-boot:run`
  Run the application in place.

- #### `./mvnw spring-boot:start`
  Start a spring application. Contrary to the run goal, this does not block and allows other goals to operate on the
  application. This goal is typically used in integration test scenario where the application is started before a test
  suite and stopped after.

- #### `./mvnw spring-boot:stop`
  Stop an application that has been started by the 'start' goal. Typically invoked once a test suite has completed.

- #### `./mvnw spring-boot:build-image`
  Package an application into a OCI image using a buildpack.

- #### `./mvnw spring-boot:build-info`
  Generate a `build-info.properties` file based on the content of the current MavenProject.

- #### `./mvnw spring-boot:help`
  Display help information on `spring-boot-maven-plugin`.
  Call `./mvnw spring-boot:help -Ddetail=true -Dgoal=<goal-name>` to display parameter details.

- #### `./mvnw spring-boot:repackage`
  Repackage existing `JAR` and `WAR` archives so that they can be executed from the command line using `java -jar`.
  With `layout=NONE` can also be used simply to package a `JAR` with nested dependencies (and no main class, so not
  executable).

- #### `./mvnw clean install`
  Run the wrapper script without requiring maven. Run `./mvnw.cmd clean install` to batch.

## Dependencies and Plugins

### Dependencies

- [graphql-java-kickstart]()
    - [graphql-spring-boot-starter](): GraphQL and GraphiQL Spring Framework Boot starter with Maven, Java Tools,
      Annotations, configs
    - [playground-spring-boot-starter](https://github.com/graphql-java-kickstart/graphql-spring-boot#enable-graphql-playground):
      GraphQL Playground access at root `/playground` which embeds a context-aware GraphQL IDE (
      GraphQL Playground React) for development workflows
    - [voyager-spring-boot-starter](https://github.com/graphql-java-kickstart/graphql-spring-boot#enable-graphql-voyager):
      GraphQL Voyager access at root `/voyager` which Represent any GraphQL API as an interactive graph.
- [graphql-java]()
    - [graphql-java-extended-scalars]()
    - [graphql-java-extended-validation]()
- [reactor-core](https://projectreactor.io/): reactive library for building non-blocking applications on the JVM
- [hibernate-validator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/):
  metadata model API from presentation to persistence layer for entity and method validation
- [spring-boot-starter-security](https://spring.io/projects/spring-security): authentication and authorization framework
  for Java apps with Servlet API integration
- [lombok](https://projectlombok.org/) Java annotation library that automatically plugs into a remote editor and build
  tools
- [junit](https://junit.org/junit5/docs/current/user-guide/): platform that serves as foundation for launching testing
  frameworks on the JVM configured to your local IDE (IntelliJ IDEA)
    - [junit-jupiter](https://junit.org/junit5/docs/current/user-guide/): combination of the new programming model and
      extension model for writing tests and extensions in [JUnit 5](https://junit.org/junit5/)
    - [junit-jupiter-api](http://junit.sourceforge.net/javadoc/): Provides classes used to describe, collect, run and
      analyze multiple tests, writing new tests and extensions.
    - [junit-jupiter-engine](): JUnit Jupiter test engine implementation, only required at runtime.
- [assertj-core](https://github.com/assertj/assertj-core): rich and intuitive set of strongly-typed assertions to use
  for unit testing with JUnit

### Plugins

- [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/):
  provides Spring Boot support in Apache Maven including packaging executable `jar` or `war`
  archives, running Spring Boot applications, generating build information and starting your Spring Boot application
  prior to running integration tests.
- [maven-resources-plugin](https://maven.apache.org/plugins/maven-resources-plugin/): Resources Plugin handles the
  copying of project resources to the output directory as disparate sets of resources: main resources and test
  resources, where main resources are the resources associated to the main source code and the test resources are
  associated to the test source code
- [maven-compiler-plugin](https://maven.apache.org/plugins/maven-compiler-plugin/): Compiler used to compile the sources
  of the project
- [mojohaus build-helper-maven-plugin](https://www.mojohaus.org/build-helper-maven-plugin/usage.html):
  Allows additional source directories to a Maven project since `pom.xml` only allows one source directory.
- [maven-surefire-plugin](https://maven.apache.org/surefire/maven-surefire-plugin/): designed to run unit tests
- [maven-failsafe-plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/): designed to run integration tests
- [maven-pmd-plugin](http://maven.apache.org/plugins/maven-pmd-plugin/): automatically run the PMD code analysis tool on
  your project's source code and generate a site report with its results

## Notes

### Async Resolvers

You can make graphql resolvers execute asynchronously by setting the return type to a
`CompletableFuture` and executing the logic within another thread. This of course does not work if you do your logic as
normal then return a `CompletableFuture.completedFuture(u)`. As here the logic processing will occur in the graphql user
thread.

Async resolvers permit the graphql server to execute multiple resolver methods in parallel. For resolvers to execute in
parallel, they must not depend on each other (parent/child fields).

Executing in parallel is useful if the client requests multiple fields that do not depend on each other and could take a
considerable amount of time to fetch. We now have response times of worst-of (resolver A latency, resolver B latency)
instead of resolver A latency + resolver B latency.

Take time to size and shape your `threadpools`, keep an eye on them over time.

Also, be aware that when you execute resolvers async, then the child resolvers will execute in it's parents async
thread (See below: standard `CompletableFuture` chain).

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

### DataLoader (N+1 Problem)

By default, a `GraphQLQueryResolver` will resolve a field's nodes sequentially. When we have a list (
see [Hashed Cursor-Based-Pagination](#hashed-cursor-based-pagination)) of nodes, the Resolver will resolve each node's
fields sequentially.

For example if we have 2 nodes (`Node1`, `Node2`) with `Field X` in the `SelectionSet`, it will execute
the `QueryResolver`
first, then the `Resolver` for `Node1 Field X` followed by `Node2 Field X`. This is the `N+1 problem`.

A small backend API latency can potentially introduce massive latency into our GraphQL API. That is with `dataloader`
neglect.

In most cases we would want to group together `Field X` values and batch them to the backing API. This leads to massive
performance gains in reduction of network traffic and optimal database queries on the datasource.

In the `context.dataloader` package, we implement `Java graphql dataloader` (a direct port
of [Facebook's dataloader](https://github.com/graphql/dataloader)) to solve this problem. We can easily
use `CompletableFutures` to load the `ID`s and using a `mapped batch dataloader`. With the `DataLoader` creation we can
customize the cache and maximum batch size with the `DataLoaderOptions`
parameter.

Often there is not a 1:1 mapping of our batch-loaded keys to the values returned. For example, an `SQL` query may return
fewer results than query `ID`s (does not exist or duplicates). If we used the classic dataloader, then the values would
be returned into the wrong requesting nodes and the last (requested `ID`s minus responded `ID`s) would have `null`
values. This is because it maps 1:1 by default.

To get around this, we can return a `map` from the `dataloader` function. When the `map` is processed by
the `DataLoader` code, any keys that are missing in the `map` will be replaced with `null` values. The semantic that
__the number of `DataLoader.load` requests are matched with an equal number of values__ is kept.

The keys provided __must__ be `first` class keys since they will be used to examine the returned map and create the list
of results, with `nulls` filling in for missing values.

To test this, implement a version
of [BankAccountResolver](src/main/java/com/example/springbootgraphql/resolver/bank/query/BankAccountResolver.java)'
s `balance` method that throws an `InterruptedException` via `Thread.sleep(x)` as per below.

```java

@Slf4j
@Component
public class BankAccountResolver implements GraphQLResolver<BankAccount> {

  public BigDecimal balance(BankAccount bankAccount) throws InterruptedException {
    Thread.sleep(3000L);
    log.info("Getting balance for {}", bankAccount.getId());
    return BigDecimal.ONE;
  }

}
```

The result is: a sequential execution of threads as opposed to un-ordered/non-sequential resolutions by pool. (Let alone
taking 11 seconds to complete).

```bash
2021-10-03 22:46:35.948  INFO 38459 --- [nio-8080-exec-2] c.e.s.listener.LoggingListener           : Received GraphQL request.
2021-10-03 22:46:35.962  INFO 38459 --- [pool-2-thread-2] c.e.s.resolver.bank.ClientResolver       : Requesting client data for bank account id c6aa269a-812b-49d5-b178-a739a1ed74cc
2021-10-03 22:46:36.464  INFO 38459 --- [nio-8080-exec-2] c.e.s.r.bank.query.BankAccountResolver   : Getting balance for c6aa269a-812b-49d5-b178-a739a1ed74cc
2021-10-03 22:46:36.469  INFO 38459 --- [pool-2-thread-3] c.e.s.resolver.bank.ClientResolver       : Requesting client data for bank account id 024bb503-5c0f-4d60-aa44-db19d87042f4
2021-10-03 22:46:36.971  INFO 38459 --- [nio-8080-exec-2] c.e.s.r.bank.query.BankAccountResolver   : Getting balance for 024bb503-5c0f-4d60-aa44-db19d87042f4
2021-10-03 22:46:36.973  INFO 38459 --- [pool-2-thread-4] c.e.s.resolver.bank.ClientResolver       : Requesting client data for bank account id 410f5919-e50b-4790-aae3-65d2d4b21c77
2021-10-03 22:46:37.475  INFO 38459 --- [nio-8080-exec-2] c.e.s.r.bank.query.BankAccountResolver   : Getting balance for 410f5919-e50b-4790-aae3-65d2d4b21c77
2021-10-03 22:46:37.477  INFO 38459 --- [pool-2-thread-5] c.e.s.resolver.bank.ClientResolver       : Requesting client data for bank account id 48e4a484-af2c-4366-8cd4-25330597473f
2021-10-03 22:46:37.981  INFO 38459 --- [nio-8080-exec-2] c.e.s.r.bank.query.BankAccountResolver   : Getting balance for 48e4a484-af2c-4366-8cd4-25330597473f
2021-10-03 22:46:37.987  INFO 38459 --- [nio-8080-exec-2] c.e.s.listener.LoggingListener           : Completed Request. Time Taken: PT2.117868S
```

Note: The resolutions are typically resolved entirely out of sequence thanks to the [Async Resolvers](#async-resolvers)

### Optimization

- [SelectionSet](https://www.graphql-java.com/documentation/v11/data-fetching/)
- [Field selection](https://www.graphql-java.com/documentation/v12/fieldselection/)
- [DataFetchingFieldSelectionSet](https://github.com/graphql-java/graphql-java/blob/master/src/main/java/graphql/schema/DataFetchingFieldSelectionSet.java)

A very powerful feature of graphql spring boot is that you have access to the query's selection set. A selection set is
the set of fields the user requested.

Having direct access to the fields can enable you to make performance optimized queries requesting only the needed data.
For example an SQL backed system may be able to use the field sub selection to only retrieve the columns that have been
asked for.

If you look ahead in the selection set, you may be able to optimize further and collapse two backend resource calls into
one. For example, if you can retrieve the sellingLocations data within the products API call. You can group everything
into one API query instead of two.

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
`contains`, `containsAnyOf`, `containsAnyOf`. These can be used as the predicate to make your API call selection.

To get the requested field names you can stream the fields, filter and collect into a set.

### Scalars

- [Extended Scalars](https://github.com/graphql-java/graphql-java-extended-scalars)

### Validation

There are two ways of validating your graphql input types, via `Directives` or manually validating the input type in
your resolver. The following shows both the more old-school approach which puts the
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

The Directive version implements `graphql-java-extended-validation` which allow us to remove all java bean validation
annotations, and instead place the annotations directly on the schema.

### Hashed Cursor-Based Pagination

Relay `@connection` directive is implemented for pagination. A matching resolver is created with the graphql java relay
`Connection`, `Edges`, `Nodes`, `PageInfo`, `StartCursor`, `EndCursor`, `isHasPreviousPage`, `isHasNextPage` and Cursor
package / classes.

Cursor-based pagination is powerful because, with opaque graphql cursors, the real cursor value can be used in
combination with the `SEEK` sql pattern (or equivalent in other data stores). Making the graphql cursor opaque (base 64
encoded) provides the additional flexibility if the pagination model changes in the future. For example, the graphql api
customers / UI only needs to change the input filters and can consistently use the cursor to offset the next pages. The
UI does not need to know about the cursors real value, which parameter to set it in and in which format. It just
supplies the edge's cursor to the 'after'.

Essentially, it is much like a `Flask-Restful-Mongo` Pagination wrapper class in Python but without a numerical value to
the page. Only an encoded pointer to `previous/current/next`.

### Custom Context

In graphql spring boot, we have the ability to create a custom object once at the very start of the query/mutation, and
this object will be available to all mutations and queries via the `DataFetchingEnvironment`. The custom object can be
any type, and is never used by the internal graphql java framework.

As the context is created once and available in all revolvers; a common use-case to store user authorization data such
as user id, permissions and roles. This data can then be used to perform authorization on our mutation and resolvers. An
alternative to this is `spring security` and pass the` security context` to other threads via
`DelegatingSecurityContextExecutorService`. But the context file can contain anything we wish.

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

Patterns in this repo, including schema types, resolvers, Java classes, GraphQL query techniques for general RDBS
performance, and opinionated settings for clean code are modeled
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