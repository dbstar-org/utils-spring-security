package io.github.dbstarll.utils.spring.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class StringAuthenticationFilter extends PreAuthenticatedAuthenticationFilter<String, String> {
    private static final RequestMatcher requestMatcher = new AntPathRequestMatcher("/auth", HttpMethod.POST.name());

    public StringAuthenticationFilter() {
        super(requestMatcher, true);
    }

    @Override
    protected String getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader("test_username");
    }

    @Override
    protected String getPreAuthenticatedCredentials(HttpServletRequest request) {
        return request.getHeader("test_password");
    }
}
