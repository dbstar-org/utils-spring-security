package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface PreAuthenticatedAuthenticationService<P, C> {
  UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken<P, C> token) throws UsernameNotFoundException;
}
