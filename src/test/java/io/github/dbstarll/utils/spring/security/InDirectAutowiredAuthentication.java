package io.github.dbstarll.utils.spring.security;

public class InDirectAutowiredAuthentication extends AutowiredStringAuthentication {
    @Override
    protected PreAuthenticatedAuthenticationFilter<String, String> originalFilter() {
        return null;
    }

    @Override
    protected PreAuthenticatedAuthenticationService<String, String> originalService() {
        return new StringAuthenticationService();
    }
}
