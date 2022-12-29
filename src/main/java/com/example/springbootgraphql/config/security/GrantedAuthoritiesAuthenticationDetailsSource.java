package com.example.springbootgraphql.config.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

/** Source class for user build roles and details */
public class GrantedAuthoritiesAuthenticationDetailsSource
    implements AuthenticationDetailsSource<
        HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {

  /**
   * Extracts the user roles from a `user_roles` header and builds authorities
   *
   * @param request {@link HttpServletRequest}
   * @return a new {@link PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails} from a list of
   *     default {@link GrantedAuthority} | existing ones via {@link #getAuthorities(String)}
   */
  @Override
  public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
      HttpServletRequest request) {

    var userRoles = request.getHeader("user_roles");
    var authorities =
        StringUtils.isBlank(userRoles) ? List.<GrantedAuthority>of() : getAuthorities(userRoles);

    return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(request, authorities);
  }

  /**
   * @param userRoles the string value from a request header of the same name
   * @return a list of {@link GrantedAuthority}, a representation of authority granted to an {@link
   *     org.springframework.security.core.Authentication Authentication} object, and must either
   *     represent itself as a string or be supported by an {@link
   *     org.springframework.security.access.AccessDecisionManager AccessDecisionManager}, mapped
   *     from {@link SimpleGrantedAuthority}, a basic `contract/role`
   */
  private List<GrantedAuthority> getAuthorities(String userRoles) {
    return Set.of(userRoles.split(",")).stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
