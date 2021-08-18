package io.github.dbstarll.utils.spring.security;

public interface PreAuthenticatedAuthentication<P, C> {
    /**
     * 获得PreAuthenticatedAuthenticationFilter.
     *
     * @return PreAuthenticatedAuthenticationFilter
     */
    PreAuthenticatedAuthenticationFilter<P, C> filter();

    /**
     * 获得PreAuthenticatedAuthenticationService.
     *
     * @return PreAuthenticatedAuthenticationService
     */
    PreAuthenticatedAuthenticationService<P, C> service();
}
