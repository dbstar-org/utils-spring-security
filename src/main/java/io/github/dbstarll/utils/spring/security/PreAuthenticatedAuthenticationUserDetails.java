package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public abstract class PreAuthenticatedAuthenticationUserDetails<P, C> implements UserDetails {
    private static final long serialVersionUID = -637710468004220183L;

    private final PreAuthenticatedAuthenticationToken token;

    protected PreAuthenticatedAuthenticationUserDetails(final PreAuthenticatedAuthenticationToken token) {
        this.token = token;
    }

    protected final PreAuthenticatedAuthenticationToken getToken() {
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
