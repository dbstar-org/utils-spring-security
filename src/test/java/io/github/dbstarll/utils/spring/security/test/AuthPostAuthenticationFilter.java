package io.github.dbstarll.utils.spring.security.test;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public final class AuthPostAuthenticationFilter extends PreAuthenticatedAuthenticationFilter<String, PostCredentials> {
    public static final RequestMatcher requestMatcher = new AntPathRequestMatcher("/auth", HttpMethod.POST.name());
    public static final String HEADER_PRINCIPAL = "auth_post_username";
    public static final String HEADER_CREDENTIALS = "auth_post_password";

    public AuthPostAuthenticationFilter() {
        super(requestMatcher, true);
    }

    @Override
    protected String getPreAuthenticatedPrincipal(final HttpServletRequest request) {
        return request.getHeader(HEADER_PRINCIPAL);
    }

    @Override
    protected PostCredentials getPreAuthenticatedCredentials(final HttpServletRequest request) {
        final String credentials = request.getHeader(HEADER_CREDENTIALS);
        return StringUtils.isBlank(credentials) ? null : new PostCredentials(credentials);
    }
}
