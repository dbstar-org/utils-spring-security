package io.github.dbstarll.utils.spring.security;

public class InDirectAuthentication implements StringAuthentication {
    @Override
    public PreAuthenticatedAuthenticationFilter<String, String> filter() {
        return null;
    }

    @Override
    public PreAuthenticatedAuthenticationService<String, String> service() {
        return new StringAuthenticationService();
    }
}
