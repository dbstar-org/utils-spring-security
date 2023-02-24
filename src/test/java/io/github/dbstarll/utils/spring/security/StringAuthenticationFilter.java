package io.github.dbstarll.utils.spring.security;

import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class StringAuthenticationFilter extends PreAuthenticatedAuthenticationFilter<String, String> {
    public StringAuthenticationFilter() {
        super(AnyRequestMatcher.INSTANCE, true);
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
