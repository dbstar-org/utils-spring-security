package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;

public abstract class PreAuthenticatedAuthenticationUserDetails<P, C> implements UserDetails {
  private static final long serialVersionUID = -637710468004220183L;

  protected final PreAuthenticatedAuthenticationToken<P, C> token;

  public PreAuthenticatedAuthenticationUserDetails(final PreAuthenticatedAuthenticationToken<P, C> token) {
    this.token = token;
  }

  @Override
  public String getUsername() {
    return token.getName();
  }

  @Override
  public String getPassword() {
    return null;
  }
}
