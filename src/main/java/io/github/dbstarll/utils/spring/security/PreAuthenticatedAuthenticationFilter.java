package io.github.dbstarll.utils.spring.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class PreAuthenticatedAuthenticationFilter<P, C> extends AbstractPreAuthenticatedProcessingFilter {
  private final RequestMatcher requestMatcher;

  public PreAuthenticatedAuthenticationFilter(RequestMatcher requestMatcher, boolean checkForPrincipalChanges) {
    this.requestMatcher = requestMatcher;
    setCheckForPrincipalChanges(checkForPrincipalChanges);
  }

  @Override
  public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    if (requestMatcher == null || requestMatcher.matches((HttpServletRequest) request)) {
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
