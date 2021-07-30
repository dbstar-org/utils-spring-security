package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public final class PreAuthenticatedAuthenticationToken<P, C> implements Authentication, CredentialsContainer {
  private static final long serialVersionUID = -6950347267051105752L;

  private final org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken token;

  public PreAuthenticatedAuthenticationToken(
          org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken token) {
    this.token = token;
  }

  @Override
  public String getName() {
    return token.getName();
  }

  @Override
  public void eraseCredentials() {
    token.eraseCredentials();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return token.getAuthorities();
  }

  @SuppressWarnings("unchecked")
  @Override
  public C getCredentials() {
    return (C) token.getCredentials();
  }

  @Override
  public Object getDetails() {
    return token.getDetails();
  }

  @SuppressWarnings("unchecked")
  @Override
  public P getPrincipal() {
    return (P) token.getPrincipal();
  }

  @Override
  public boolean isAuthenticated() {
    return token.isAuthenticated();
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    token.setAuthenticated(isAuthenticated);
  }
}
