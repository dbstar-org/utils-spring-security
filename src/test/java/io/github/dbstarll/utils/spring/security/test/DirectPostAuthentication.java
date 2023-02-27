package io.github.dbstarll.utils.spring.security.test;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationService;

public class DirectPostAuthentication implements PreAuthenticatedAuthentication<String, PostCredentials> {
    @Override
    public PreAuthenticatedAuthenticationFilter<String, PostCredentials> filter() {
        return new AuthPostAuthenticationFilter();
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
    public Class<PostCredentials> getCredentialsClass() {
        return PostCredentials.class;
    }
}