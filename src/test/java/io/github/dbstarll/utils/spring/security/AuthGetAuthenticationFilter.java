package io.github.dbstarll.utils.spring.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public final class AuthGetAuthenticationFilter extends PreAuthenticatedAuthenticationFilter<String, String> {
    public static final RequestMatcher requestMatcher = new AntPathRequestMatcher("/auth", HttpMethod.GET.name());
    public static final String HEADER_PRINCIPAL = "auth_get_username";
    public static final String HEADER_CREDENTIALS = "auth_get_password";

    public AuthGetAuthenticationFilter() {
        super(requestMatcher, true);
    }

    @Override
    protected String getPreAuthenticatedPrincipal(final HttpServletRequest request) {
        return request.getHeader(HEADER_PRINCIPAL);
    }

    @Override
    protected String getPreAuthenticatedCredentials(final HttpServletRequest request) {
        return request.getHeader(HEADER_CREDENTIALS);
    }
}
