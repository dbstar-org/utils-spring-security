package io.github.dbstarll.utils.spring.security;

public class AutowiredAuthentication extends AutowiredPreAuthenticatedAuthentication<String, String> {
    public AutowiredAuthentication() {
        super(String.class, String.class);
    }

    @Override
    protected PreAuthenticatedAuthenticationFilter<String, String> originalFilter() {
        return new StringAuthenticationFilter();
    }

    @Override
    protected PreAuthenticatedAuthenticationService originalService() {
        return new StringAuthenticationService();
    }
}
