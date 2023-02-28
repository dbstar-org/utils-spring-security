package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public interface PreAuthenticatedAuthenticationService<P, C> extends
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    @SuppressWarnings("unchecked")
    @Override
    default UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return loadUserDetails((P) token.getPrincipal(), (C) token.getCredentials(), token);
    }

    /**
     * @param principal   The Principal of pre-authenticated authentication token
     * @param credentials The Credentials of pre-authenticated authentication token
     * @param token       The pre-authenticated authentication token
     * @return UserDetails for the given authentication token, never null.
     * @throws UsernameNotFoundException if no user details can be found for the given
     *                                   authentication token
     */
    UserDetails loadUserDetails(P principal, C credentials, PreAuthenticatedAuthenticationToken token)
            throws UsernameNotFoundException;
}
