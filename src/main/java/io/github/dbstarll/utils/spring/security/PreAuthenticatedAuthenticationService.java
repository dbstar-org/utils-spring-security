package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public interface PreAuthenticatedAuthenticationService extends
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
}
