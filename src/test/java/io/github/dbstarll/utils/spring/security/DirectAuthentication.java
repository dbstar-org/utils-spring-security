package io.github.dbstarll.utils.spring.security;

public class DirectAuthentication implements PreAuthenticatedAuthentication<String, String> {
    @Override
    public PreAuthenticatedAuthenticationFilter<String, String> filter() {
        return null;
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