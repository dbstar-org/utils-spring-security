package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;

public abstract class PreAuthenticatedAuthenticationUserDetails<P, C> implements UserDetails {
    private static final long serialVersionUID = -637710468004220183L;

    private final PreAuthenticatedAuthenticationToken<P, C> token;

    protected PreAuthenticatedAuthenticationUserDetails(final PreAuthenticatedAuthenticationToken<P, C> token) {
        this.token = token;
    }

    protected final PreAuthenticatedAuthenticationToken<P, C> getToken() {
        return token;
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
