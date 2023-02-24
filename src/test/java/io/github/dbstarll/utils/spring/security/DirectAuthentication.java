package io.github.dbstarll.utils.spring.security;

import org.springframework.security.authentication.AuthenticationManager;

public class DirectAuthentication implements PreAuthenticatedAuthentication<String, String> {
    @Override
    public PreAuthenticatedAuthenticationFilter<String, String> filter(AuthenticationManager authenticationManager) {
        final PreAuthenticatedAuthenticationFilter<String, String> filter = new StringAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    public PreAuthenticatedAuthenticationService service() {
        return new StringAuthenticationService();
    }

    @Override
    public Class<String> getPrincipalClass() {
        return String.class;
    }

    @Override
    public Class<String> getCredentialsClass() {
        return String.class;
    }
}