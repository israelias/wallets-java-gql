package com.example.springbootgraphql.config.security;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * PreAuthentication filter extends the Spring base class for processing filters that handle
 * pre-authenticated authentication requests, where it is assumed that the principal has already
 * been authenticated by an external system.
 */
public class RequestHeadersPreAuthenticationFilter
    extends AbstractPreAuthenticatedProcessingFilter {

  /**
   * Extract the principal information from the current request
   *
   * <p>currently we expect it from `user_id` and we can trust it because it is coming from our
   * sidecar
   *
   * @param request {@link HttpServletRequest}
   * @return a given request header's string value
   */
  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    return request.getHeader("user_id");
  }

  /**
   * Extract the credentials from the current request. Should not return null for a valid principal,
   * though some implementations may return a dummy value.
   *
   * <p>We don't use credentials, since we're using tokens.
   *
   * @param request {@link HttpServletRequest}
   * @return an empty string
   */
  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return StringUtils.EMPTY;
  }
}
