package com.example.springbootgraphql.config.security;

import javax.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

/**
 * Secure our GraphQL with a Config class that extends {@link WebSecurityConfigurerAdapter}
 *
 * <p>with {@link EnableWebSecurity}
 */
@Slf4j
@Configuration
@EnableWebSecurity // Debug support will print the execution of the FilterChainProxy
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class GraphQLSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Override the configure method to create a {@link PreAuthenticatedAuthenticationProvider}
   *
   * @param authenticationManagerBuilder
   */
  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
    authenticationManagerBuilder.authenticationProvider(preAuthenticatedAuthenticationProvider());
  }

  /**
   * Set up the SpringSecurity endpoint by adding a preAuthentication filter.
   *
   * @param httpSecurity {@link HttpSecurity}
   * @throws Exception runtime exception
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    log.info("Securing all endpoints");

    httpSecurity
        // Add the Pre Authentication Filter, processing is #1
        .addFilterBefore(
            createRequestHeadersPreAuthenticationFilter(),
            AbstractPreAuthenticatedProcessingFilter.class)
        .authorizeRequests()
        // All endpoints require authentication
        .anyRequest()
        .authenticated()
        .and()
        // Disable CSRF Token generation
        .csrf()
        .disable()
        // Disable the default HTTP Basic-Auth
        .httpBasic()
        .disable()
        // Disable the session management filter
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // Disable the /logout filter
        .logout()
        .disable()
        // Disable anonymous users
        .anonymous()
        .disable();
  }

  /**
   * Permit actuator health endpoint for uptime checks. Web ignoring will bypass the FilterChain.
   *
   * @param web {@link WebSecurity}
   */
  @Override
  public void configure(WebSecurity web) {

    web.ignoring()
        // Actuator health endpoint for readiness, liveness checks etc
        .antMatchers("/actuator/health")
        // Permit playground for development
        .antMatchers("/playground", "/vendor/playground/**")
        // Subscription are secured via AuthenticationConnectionListener
        .antMatchers("/subscriptions");
    ;
  }

  /**
   * PreAuth filter
   *
   * @return filter a new {@link RequestHeadersPreAuthenticationFilter} with a new {@link
   *     GrantedAuthoritiesAuthenticationDetailsSource} set.
   * @throws Exception if any of the filter conditions are not met during a request
   * @see AbstractPreAuthenticatedProcessingFilter#setAuthenticationManager
   * @see AbstractPreAuthenticatedProcessingFilter#doFilter
   */
  private Filter createRequestHeadersPreAuthenticationFilter() throws Exception {
    // new filter that extracts username from principal
    var filter = new RequestHeadersPreAuthenticationFilter();
    // The AuthenticationDetailsSource to use to match/merge principal
    filter.setAuthenticationDetailsSource(new GrantedAuthoritiesAuthenticationDetailsSource());
    // The AuthenticationManager to use
    filter.setAuthenticationManager(authenticationManager());
    // if true, and AuthenticationException raised will be swallowed and the request will continue,
    // potentially using alternate catchers.
    // if false, authentication failure will result in immediate exception
    filter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
    return filter;
  }

  /**
   * Creates a provider through which we can call {@link
   * PreAuthenticatedAuthenticationProvider#authenticate(Authentication)}} which extracts out the
   * user details, verifies them, and if correct, builds a pre-auth and returns a token
   *
   * @return preAuthProvider a new {@link PreAuthenticatedAuthenticationProvider} with an {@link
   *     org.springframework.security.core.userdetails.AuthenticationUserDetailsService
   *     AuthenticationUserDetailsService} set to load a {@link
   *     org.springframework.security.core.userdetails.UserDetails UserDetails} for an authenticated
   *     user object created by a new {@link PreAuthenticatedGrantedAuthoritiesUserDetailsService}
   *     based on an authentication token, never null
   */
  public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
    var preAuthProvider = new PreAuthenticatedAuthenticationProvider();
    preAuthProvider.setPreAuthenticatedUserDetailsService(
        new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
    return preAuthProvider;
  }
}
