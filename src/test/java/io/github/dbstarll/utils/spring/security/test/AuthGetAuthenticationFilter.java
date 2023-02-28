package io.github.dbstarll.utils.spring.security.test;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public final class AuthGetAuthenticationFilter extends PreAuthenticatedAuthenticationFilter<String, GetCredentials> {
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
    protected GetCredentials getPreAuthenticatedCredentials(final HttpServletRequest request) {
        final String credentials = request.getHeader(HEADER_CREDENTIALS);
        return StringUtils.isBlank(credentials) ? null : new GetCredentials(credentials);
    }
}
