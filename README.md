# Spring Boot GraphQL Wallets App

Full stack CRUD app that stores personal asset references in a secure wallet
provider (`BankAccount`)
built with Java [Spring Boot](https://spring.io/projects/spring-boot)
and [GraphQL Java](https://www.graphql-java.com/) bootstrapped
with [GraphQL Java Kickstart](https://github.com/graphql-java-kickstart) managed, bundled, tested
via [Maven](https://maven.apache.org/)

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

## PMD on Maven

- [Maven PMD Plugin Default](https://github.com/apache/maven-pmd-plugin/blob/master/src/main/resources/rulesets/java/maven-pmd-plugin-default.xml)
- [Spring Initializer Docs](https://docs.spring.io/initializr/docs/0.4.x/reference/htmlsingle/)
- [PMD Rule Configuration](https://pmd.github.io/latest/pmd_userdocs_configuring_rules.html)
- [PMD Making Rulesets](https://pmd.github.io/latest/pmd_userdocs_making_rulesets.html)

## Repo Benchmark

- [Philip Starritt Spring Boot GraphQL Tutorial](https://www.youtube.com/c/PhilipStarritt/videos)