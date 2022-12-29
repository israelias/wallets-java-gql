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

### DataLoader Key Context

By default in GraphQL, a query resolver will resolve a field's nodes sequentially. When you have a list (
see [Connection cursor](#hashed-cursor-based-pagination)) of nodes, It will resolve each node's fields sequentially. For
example if you have 2 nodes with field X in the selection set. It will execute the query resolver first, then resolver
for Node1 Field X followed by Node2 Field X. This is the N+1 problem.

We figured out how to solve the above dataloader n+1 problem in [DataLoader N+1 Problem](#dataloader-n1-problem)

BUT Now we have another problem!

What happens if we require some additional fields from the `BankAccount` (Context) inside the dataloader function? How
do we pass them into the dataloader?

We enrich the `Balance` response with another field contained the BankAccount. This final value would then be set into
the `dataloader`'s return `Map`.

To solve this problem, we can load the `ID` with a context into the dataloader. In this example, the context is
`BankAccount`. The `IDs` mapped to `Contexts` are accessible inside the dataloader function via the
`BatchLoaderEnvironment#getKeyContexts`. Unfortunately this returns a `Map(Object, Object)` instead of the preferred
generic types, so we lose type safety. To get around this, we _cast_ this to `Map`, then we pass the `Map` into our
function with
`Map(UUID, BankAccount)` defined as the method parameters.

But, why don't we just load the BankAccount into the dataloader? This sounds good and easy - BUT If you do that, then we
no-longer get the speed from the UUID hashmap lookups. So we always use the most efficient key possible.

- [Mapped Batched DataLoader](https://github.com/graphql-java/java-dataloader#returning-a-map-of-results-from-your-batch-loader)
- [GraphQL DataLoader from Facebook](https://github.com/graphql/dataloader)

### Optimization

- [GraphQL Java DataLoader](https://github.com/graphql-java/java-dataloader)
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

### Instrumentation

GraphQL Instrumentation allows us to inject code that can observe the execution of a query and also change the runtime
behaviour. `RequestLogginInstrumentation` takes advantage of this to log the graphql request's query, variables, any
exceptions thrown and execution time.

To do this we create a `spring bean` of type `graphql.execution.instrumentation.Instrumentation`. To remove boilerplate
no-op code, we override no-op `SimpleInstrumentation` implementation and just override the `beginExecution` method.

We then return a `SimpleInstrumentationContext` with a `whenCompleted` callable containing our duration logic and log
message.

It is possible that multiple Instrumentation objects are in one graphql server.

In the next video, I demo the TracingInstrumentation that will capture the execution time of the fields and place the
results into the graphql response.

### Tracing Instrumentation (Request Tracing)

In a traditional REST API, it is common to add percentile response time monitoring on the URL level. In graphql, things
are slightly different. All requests go via one endpoint, `/graphql`. Placing a percentile on the HTTP URL would not
give us any useful information. So how do we identify slow resolvers and bottlenecks?

One method is to add request tracing to your `resolvers/datafetchers`, `dataloaders` and `fields`. This tracing will
record important information such as the execution time. This data can then be used to identify slow revolvers that need
tuned.

Although, beware If a resolver throws an exception, its child resolvers will not execute - therefore you may have
slightly misleading metrics / missing data for that period of time (until parent resolver is not throwing exceptions).

We can enable the `TracingInstrumentation` bean by setting the `graphql.servlet.tracingEnabled: true`. This will record
the execution time of our `resolvers/field` methods. This data will be placed into the graphql response object and
visible via graphql playground.

Beware in production, this can add considerable latency to response times in high-throughput APIs. we might want to have
a play about first... or do some sampling.

### Correlation ID (Thread Propagation)

In a multi-threaded graphql server it is imperative to propagate a `request correlation id` to all threads invoked. This
will ensure all threads can log the correlation id which will then provide insight into the code flow and ultimately
which request triggered that section of code. If we log the original graphql query and variables with the correlation
ID, we can now link any log to the original graphql request. Very useful for debugging then exceptions.

We first create an `Instrumentation` class and override the `beginExecution` method. Inside this method we assign a
unique correlation id to the loggers `mapped diagnostic context` (MDC). As our graphql service is an edge node, no
upstream service will provide a correlation id to graphql. We therefore can use the graphql `Execution ID`. This a UUID
generated by the graphql framework for each request. We then return a `whenCompleted` callback to clear the `MDC`. It’s
important to do this as we want to ensure the `MDC` does not contain any values from the previous request. We can set an
additional
`MDC.clear` on a listener’s `onComplete()` method to ensure the original `NIO thread` has been cleared. As an
alternative, the listener `onRequest()` method can also be used to set the original `MDC` on the `tomcat NIO` thread.

We then customize the logback console appender to print the `%X{correlation_id}` with every log line.

This will allow us to paste a `correlation id` into our log viewer tool and get every log line.
E.g. `Splunk, Sumo-logic & Logentries.`

But wait! At this stage, the `MDC` is only tied to the` NIO tomcat thread`. How do we propagate it to all threads such
as
`dataloaders` and `async completable futures`?

To solve this, we create a `Correlation ID propagation executor service`. This class will get the `MDC` correlation ID
from the current thread and _set it into the `MDC` map_ in the new executing thread in the target thread pool. Right
before the thread is ready to accept another unit of work, we _clear_ the `MDC` map. This can then be chained throughout
all threads and will propagate and clear the MDC as required.

Now we will have a consistent `correlation_id` printed in all our application logs. For good monitoring and debugging.

Steps:

- Add `correlation_id` to `MDC`
- Print the `correlation_id` in the logs
- Pass the `correlation_id` to async `CompletableFutures` resolver threads
- pass the `correlation_id` to batched `dataLoader` threads
- Ensure `correlation_id` is cleared

- Previous

```xml
${CONSOLE_LOG_PATTERN:-%clr(%d{${LOG_DATEFORMAT_PATTERN:-yyyy-MM-dd HH:mm:ss.SSS}}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}
```

- New

```yaml
-%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID:}){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %clr(%X{correlation_id}){red} %m%n%wEx
```

- [Mapped Diagnostic Context: Uniquely stamping each request](http://logback.qos.ch/manual/mdc.html)

### Integration Testing (GraphQLTestTemplate)

While developing a graphql server it is equally important to develop our integration tests. This can be achieved in java
spring boot with the class `GraphQLTestTemplate` and the `graphql-spring-boot-starter-test` dependency.

But first, why do we need integration tests? Having a set of integration tests for our graphql server will allow us to
make changes with confidence. These help ensure that we didn't introduce any bugs or regressions. We can be more
comfortable when performing upgrades in dependencies, especially major bumps. Ensure that we didn't change the graphql
serialized json as this could easily break clients. Ensure our business functionality actually works as expected. Ensure
downstream services are called (`wiremock`, `grpcmock`). New developer teammates can join the product and see sample
requests and responses. They can be more comfortable making changes at an early stage. Overall, development will go
faster.

Approach:

We first create a `src/it/java` and `src/it/resources` packages and update our maven build to include those tests. Here
we include the following plugins to our POM: `maven-resources-plugin`, `build-helper-maven-plugin`
, `maven-failsafe-plugin` and
`testResources`.

We then create a `IT test class` per resolver. This test will start an `in-memory` graphql spring boot server. We then
create graphql queries together with the expected responses and copy/paste them into files in the resources package. We
then use `junit5` to create unit tests that will each load a graphql query, submit it to the server using
`GraphQLTestTemplat`e. We then can load the matching expected graphql response file from the `classpath` and `assert` it
equals the response. This can be achieved with library `skyscreamer` json `assertEquals`.

For dynamic elements such as a clock (`LocalDateTime`, `Localdate` etc.), we create the `ApplicationContext` with
a `FixedClock`. This better than mocking the clock as having the `fixedclock` as `@bean` will keep our spring
application context clean, thus avoid having to restart between tests.

It is extremely important that we do not have any mocks or spy beans in the integration tests. As they will cause the
context to dirty, therefore requiring a restart of the server between junit tests and slowing down the build
considerably.

Warning: `tracing` should be `false` when testing.

- [GraphQLTestTemplate](https://github.com/graphql-java-kickstart/graphql-spring-boot/blob/master/graphql-spring-boot-test/src/main/java/com/graphql/spring/boot/test/GraphQLTestTemplate.java)
- [grpc mock](https://github.com/Fadelis/grpcmock)

Potential Mock implementation

```kotlin

import com.example.springbootgraphql.Application;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [Application::class])
class DatabaseStatusIntegrationTest @Autowired constructor(
        val testTemplate: GraphQLTestTemplate,
        val versionRepository: VersionRepository
) {
    @Test
    @WithMockUser(username = "cwicks", authorities = ["admin"])
    fun `Test databaseStatus expect ok`() {
        val result = testTemplate.postForResource("graphql/test-dbstatus.graphql")

        assertThat(result.isOk, equalTo(true))
    }
}

```

Fixing the autowire issue

```java
@Autowired
private GraphQLTestTemplate graphQLTestTemplate;

@MockBean
private ResourceService resourceServiceMock;

@Test
public void getResourceById()throws IOException{
    Resource resource=new Resource(1L,"Facebook","facebook.com");
    when(resourceServiceMock.getResource(any())).thenReturn(resource);
    GraphQLResponse response=graphQLTestTemplate.postForResource("graphql/get-resource-by-id.graphql");
    assertTrue(response.isOk());

    assertEquals("1",response.get("$.data.resource.id"));
    assertEquals("Facebook",response.get("$.data.resource.title"));
    assertEquals("facebook.com",response.get("$.data.resource.url"));
    }
```

### JVM Profiling

Now that we have a functional GraphQL Java server running with some business logic, it's time to profile the JVM!!

Profiling our java application has massive benefits, such as increasing the chance of finding redundant memory
allocation and memory leaks. The first being critical for low latent and highly concurrent APIs.

In this section we plant a memory allocation bug and identify it with `VisualVM`.

The first thing we do is set up a small `Apache JMeter` load testing script for our GraphQL requests. We configure this
to send 3 concurrent batches of 1000 requests.

We then start our spring boot graphql server with the `VisualVM IntelliJ` launcher. Navigate to your application and
select the profile tab. Open settings and add an asterisk symbol to record all classes/methods. Click start profiling.

Execute the Jmeter script. Navigate back to VisualVM and stop the profiler.

You should now have insight into hot stack traces, heavy method calls and heap memory allocation. If you see "Unknown",
restart VisualVM... sometimes it has this bug.

`VisualVM` identified the memory allocation bug and told us the exact class and method. Thanks!!!

You can also try the sampling tab for memory allocation and CPU profiling.

Other than the above, there are many kinds of application metrics you should consider such as

- Service percentiles
- Service availability / uptime
- Internal http/grpc client percentiles
- Executor metrics (queue depth etc)
- GC execution Kubernetes memory requests
- JVM memory
- Method counts
- Exception counts / classes
- GraphQL queries / execution time
- GraphQL resolver execution time / counts
- Kubernetes CPU requests
- Database connection pools
- etc etc etc!

Other tools I like are, `micrometer`, `stackdriver`, `java mission control` and `gatling load testing`.

Summary:

- Why?: Memory leak, heavy memory allocation by over allocating objects.
- Focus: Method calls, Memory usage
- Solution:
    - Load testing: Apache JMeter
    - JVM Profiling: VisualVM (with IntelliJ plugin)
- Plant the bug:

```java
    private final Set<BigDecimal> bigCrazy=new HashSet<>();
    var size=ThreadLocalRandom.current().nextInt(250,500);
    var littleCrazy=new LinkedHashSet<BigDecimal>(size);

    IntStream.range(0,size).forEach(nextInt->littleCrazy.add(BigDecimal.valueOf(nextInt)));
    bigCrazy.addAll(littleCrazy);

```

[VisualVM for graphql jvm profiling](https://visualvm.github.io/)
[Apache Jmeter for load and performance testing](https://jmeter.apache.org/)
[Gatling load testing](https://gatling.io/)

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

### Spring Security Pre-Authorization

First Read [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture).
> > Delegate the security to the Service class that holds/pools the data/has access to it. Recommended to have policies at the actual ldata level, where the data lives, not the actual server.

running `SecurityContextHolder.getContext()` will expose the permissions that query is validated against (it's in the
normal tomcat thread). However, if we run this in a child resolver like `balanceService` (which executes in a thread
pool), we get null on `authentication` context. But we may need the security context in there to perform validation, or
passage credentials to external services or pass that JWT to external services -- so how do we propagate that into there
and how do we get that in any async stuff? Wrap the propogationid with a security wrapper.

In order to use `pre-auth`, we must ensure that all graphql requests have been previously authorized by an upstream
service. For example, All Ingres traffic to this graphql service must bypass an upstream proxy node that will validate
the request’s `JWT` token. If the `JWT` token is valid, then the proxy will forward the request into this graphql
server. This code alone provides no authorization and is not production ready alone. Read more about pre-auth patterns
before using this.

This proxy pattern is getting more common among `kubernetes` / `service-mesh` / `side car` enabled technologies such
as [istio](https://istio.io/latest/docs/concepts/security/arch-sec.svg). There are many sidecar proxy options such as
[Kong](https://konghq.com/) or [Envoy](https://www.envoyproxy.io/).

There are many benefits of this pattern, as we can extract boilerplate, hard to maintain, error-prone authorization
configuration OUT of our application code.

This allows for a clearer separation of concerns. Authorization policies and mutual TLS in one world, and application
code in another.

For a more old-school spring security approach where the real auth code lives in the application code (with explanation)
, see my other [spring security REST](https://www.youtube.com/watch?v=rOnoKiH97Nc)

Forwarding additional headers containing the extracted authorization data may allow for a smoother transition to support
other authentication methods. For example, if we can avoid our app code from decoding `JWT` `base64` or having specific
auth logic - then we can focus on the _sidecar auth migration_ with minimal application code changes. Just think if we
have 30+ microservices? An alternative would be to support a shared library and bump the app’s dependencies first.

The spring security context is _thread bound_. Luckily we can propagate the spring security context into our thread
pools and executors. This will allow us to have _spring security annotations_ and _method level security_ on _
asynchronous threads_
/ _execution_. For example `@Async`. We can propagate the security context by wrapping our executor in Spring's
[DelegatingSecurityContextExecutorService](https://docs.spring.io/spring-security/site/docs/4.2.15.RELEASE/apidocs/org/springframework/security/concurrent/DelegatingSecurityContextExecutorService.html)
.

Remember to mark the `PreAuthenticatedAuthenticationProvider` as a Spring Bean. Otherwise, you see the default spring
security fallback user/password on the logs.

Potentially:

- Get an access token with the OAuth API
- Whitelist an Ethereum address with the Raindrop API
- Request a challenge with the Raindrop API
- Execute an authentication transaction via the Server-side Raindrop smart contract
- Validate the authentication attempt with the Raindrop API

### Schema Directive Validation

This library provides schema validation via a range of directives that follow the `JSR-303 `bean validation
name/pattern.

In order to activate the directive validation with spring boot graphql, we must first create a `ValidationSchemaWiring`
bean.

There are a range of annotations we can add to your schema. As of 2020/11/29 they are:
`@AssertFalse`,` @AssertTrue`, `@DecimalMax`, `@DecimalMin`, `@Digits`, `@Expression`, `@Max`, `@Min`, `@Negative`
, `@NegativeOrZero`,
`@NotBlank`, `@NotEmpty`, `@Pattern`, `@Positive`, `@PositiveOrZero`, `@Range` and `@Size`.

To customize the error messages:

- Create a `ValidationMessages.properties` file, add it to the classpath
- Add `K,V` pairs to `ValidationMessages.properties` in the format:  `key:custom error message`
- Set the` @Directive(message: “key”) t`

Things To Note about this library

- Dependency 15.0.3/2 is not available on common maven repositories.
- Schema Validation does not trigger on nested Input Types (deal-breaker for me)
- All directives are validated. Allowing for a full object validation.
- Locale support and externalized custom messages
- Powerful expression language with @Expression
- Overall good quality of annotations. Practising what they preach.
- Integates easily with spring boot graphql. But no auto-config based on the dependency.

### Subscription With Reactor

A GraphQL subscription is a long-lasting read operation over the `WebSocket` protocol. Clients connected to a
subscription will be pushed `0-n` messages from the server. As messages will be pushed directly from the server, clients
no longer need to poll GraphQL queries checking for updates.

Pushing server events to the graphql subscription subscribers.

We use `project reactor` to create a `pubsub` that will push the mutation events to active subscriptions. When an event
occurs, the event will be published to the `FluxSink`, the `FluxProcessor` is subscribed to the `FluxSink`.

Each connected graphql subscription will call `.subscribe()` on the returned` Publisher[BankAccount] (FluxProcessor)`.
This will trigger the flow of data in the whole processor chain. Flux operations can be reused as each operation (method
call on processor) adds behavior and wraps the previous step’s publisher into a new instance. As there can be multiple
subscribers reusing parts of the chain, _you should keep operations stateless_. The original published event/message
will play through each subscriber's full publisher processing chain. See linked project reactor docs for more details,
section 3.3.3 operators and section 3.3.4 Nothing happens until you subscribe().

Based on the above, we can create two graphql subscriptions:

- One that will receive all (global) events.
- And another that will only receive events for a particular `Id` (provided by the client).

[Project Reactor Operators](https://projectreactor.io/docs/core/release/reference/#_operators)
[Project Reactor Subscribe ](https://projectreactor.io/docs/core/release/reference/#reactive.subscribe)

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

### Misc

- [GraphQL Connections](https://www.apollographql.com/blog/graphql/explaining-graphql-connections/)
- [Cursors](https://javamana.com/2021/01/20210119090856618r.html)