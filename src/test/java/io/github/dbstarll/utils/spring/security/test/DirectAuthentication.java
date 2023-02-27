package io.github.dbstarll.utils.spring.security.test;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationService;

public class DirectAuthentication implements PreAuthenticatedAuthentication<String, GetCredentials> {
    @Override
    public PreAuthenticatedAuthenticationFilter<String, GetCredentials> filter() {
        return new AuthGetAuthenticationFilter();
    }

    @Override
    public PreAuthenticatedAuthenticationService<String, GetCredentials> service() {
        return new StringAuthenticationService<>();
    }

    @Override
    public Class<String> getPrincipalClass() {
        return String.class;
    }

    @Override
    public Class<GetCredentials> getCredentialsClass() {
        return GetCredentials.class;
    }
}