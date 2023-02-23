package io.github.dbstarll.utils.spring.security;

public class DirectAutowiredAuthentication extends AutowiredPreAuthenticatedAuthentication<String, String> {
    public DirectAutowiredAuthentication() {
        super(String.class, String.class);
    }

    @Override
    protected PreAuthenticatedAuthenticationFilter<String, String> originalFilter() {
        return null;
    }

    @Override
    protected PreAuthenticatedAuthenticationService<String, String> originalService() {
        return new StringAuthenticationService();
    }
}
