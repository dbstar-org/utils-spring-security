package io.github.dbstarll.utils.spring.security;

public interface PreAuthenticatedAuthentication<P, C> {
  PreAuthenticatedAuthenticationFilter<P, C> filter();

  PreAuthenticatedAuthenticationService<P, C> service();
}
