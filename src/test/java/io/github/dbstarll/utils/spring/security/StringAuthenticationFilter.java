package io.github.dbstarll.utils.spring.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class StringAuthenticationFilter extends PreAuthenticatedAuthenticationFilter<String, String> {
    public StringAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super(AnyRequestMatcher.INSTANCE, true);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected String getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return null;
    }

    @Override
    protected String getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }
}
