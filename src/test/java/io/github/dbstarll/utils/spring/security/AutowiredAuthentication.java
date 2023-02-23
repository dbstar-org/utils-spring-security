package io.github.dbstarll.utils.spring.security;

public class AutowiredAuthentication extends AutowiredPreAuthenticatedAuthentication<String, String> {
    private PreAuthenticatedAuthenticationFilter<String, String> originalFilter = new StringAuthenticationFilter();
    private PreAuthenticatedAuthenticationService originalService = new StringAuthenticationService();

    public AutowiredAuthentication() {
        super(String.class, String.class);
    }

    @Override
    protected PreAuthenticatedAuthenticationFilter<String, String> originalFilter() {
        return originalFilter;
    }

    public void setOriginalFilter(final PreAuthenticatedAuthenticationFilter<String, String> originalFilter) {
        this.originalFilter = originalFilter;
    }

    @Override
    protected PreAuthenticatedAuthenticationService originalService() {
        return originalService;
    }

    public void setOriginalService(final PreAuthenticatedAuthenticationService originalService) {
        this.originalService = originalService;
    }
}
