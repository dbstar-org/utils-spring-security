package io.github.dbstarll.utils.spring.security;

import org.springframework.security.authentication.AuthenticationManager;

public interface PreAuthenticatedAuthentication<P, C> {
    /**
     * 获得PreAuthenticatedAuthenticationFilter.
     *
     * @param authenticationManager AuthenticationManager实例
     * @return PreAuthenticatedAuthenticationFilter
     */
    PreAuthenticatedAuthenticationFilter<P, C> filter(AuthenticationManager authenticationManager);

    /**
     * 获得PreAuthenticatedAuthenticationService.
     *
     * @return PreAuthenticatedAuthenticationService
     */
    PreAuthenticatedAuthenticationService service();

    /**
     * 获得Principal的Class类型.
     *
     * @return class of the Principal
     */
    Class<P> getPrincipalClass();

    /**
     * 获得Credentials的Class类型.
     *
     * @return class of the Credentials
     */
    Class<C> getCredentialsClass();
}
