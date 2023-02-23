package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public abstract class PreAuthenticatedAuthenticationUserDetails<P, C> implements UserDetails {
    private static final long serialVersionUID = -637710468004220183L;

    protected final PreAuthenticatedAuthenticationToken token;

    protected PreAuthenticatedAuthenticationUserDetails(final PreAuthenticatedAuthenticationToken token) {
        this.token = token;
    }

    @SuppressWarnings("unchecked")
    protected final P getPrincipal() {
        return (P) token.getPrincipal();
    }

    @SuppressWarnings("unchecked")
    protected final C getCredentials() {
        return (C) token.getCredentials();
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
