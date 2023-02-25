package io.github.dbstarll.utils.spring.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class PreAuthenticatedAuthenticationFilter<P, C> extends AbstractPreAuthenticatedProcessingFilter {
    private final RequestMatcher requestMatcher;

    protected PreAuthenticatedAuthenticationFilter(final RequestMatcher requestMatcher,
                                                   final boolean checkForPrincipalChanges) {
        this.requestMatcher = notNull(requestMatcher, "requestMatcher must not null");
        setCheckForPrincipalChanges(checkForPrincipalChanges);
    }

    /**
     * 获得filter对应的RequestMatcher.
     *
     * @return RequestMatcher实例
     */
    public final RequestMatcher getRequestMatcher() {
        return requestMatcher;
    }

    @Override
    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (requestMatcher.matches((HttpServletRequest) request)) {
            super.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    protected abstract P getPreAuthenticatedPrincipal(HttpServletRequest request);

    @Override
    protected abstract C getPreAuthenticatedCredentials(HttpServletRequest request);
}
