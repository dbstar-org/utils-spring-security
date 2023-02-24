package io.github.dbstarll.utils.spring.security;

import org.springframework.security.authentication.AuthenticationManager;

public class AutowiredAuthentication extends AutowiredPreAuthenticatedAuthentication<String, String> {
    private PreAuthenticatedAuthenticationService originalService = new StringAuthenticationService();

    public AutowiredAuthentication() {
        super(String.class, String.class);
    }

    @Override
    protected PreAuthenticatedAuthenticationFilter<String, String> originalFilter(
            final AuthenticationManager authenticationManager) {
        return new StringAuthenticationFilter(authenticationManager);
    }

    @Override
    protected PreAuthenticatedAuthenticationService originalService() {
        return originalService;
    }

    public void setOriginalService(final PreAuthenticatedAuthenticationService originalService) {
        this.originalService = originalService;
    }
}
