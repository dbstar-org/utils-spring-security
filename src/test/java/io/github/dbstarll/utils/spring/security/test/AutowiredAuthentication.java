package io.github.dbstarll.utils.spring.security.test;

import io.github.dbstarll.utils.spring.security.AutowiredPreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationService;

public class AutowiredAuthentication extends AutowiredPreAuthenticatedAuthentication<String, PostCredentials> {
    public AutowiredAuthentication() {
        super(String.class, PostCredentials.class);
    }

    @Override
    protected PreAuthenticatedAuthenticationFilter<String, PostCredentials> originalFilter() {
        return new AuthPostAuthenticationFilter();
    }

    @Override
    protected PreAuthenticatedAuthenticationService<String, PostCredentials> originalService() {
        return new StringAuthenticationService<>();
    }
}
