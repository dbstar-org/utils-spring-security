package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

final class PreAuthenticatedAuthenticationFilterConfigurerAdapter
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private static final Class<? extends Filter> FIRST_AFTER_FILTER = AbstractPreAuthenticatedProcessingFilter.class;

    private final Map<Class<? extends Filter>, PreAuthenticatedAuthenticationFilter<?, ?>> filters;
    private final RequestMatcher requestMatcher;

    PreAuthenticatedAuthenticationFilterConfigurerAdapter(
            final List<PreAuthenticatedAuthentication<?, ?>> authentications, final ApplicationContext ctx) {
        this.filters = new ConcurrentHashMap<>();
        authentications.stream()
                .map(auth -> PreAuthenticatedAuthenticationWrapper.wrap(auth, ctx).filter())
                .forEach(filter -> this.filters.compute(filter.getClass(), (filterClass, existFilter) -> {
                    if (existFilter == null) {
                        return filter;
                    } else {
                        throw new IllegalArgumentException(String.format("Duplicate %s: %s <--> %s",
                                filterClass.getName(), existFilter, filter));
                    }
                }));
        this.requestMatcher = new OrRequestMatcher(this.filters.values().stream()
                .map(PreAuthenticatedAuthenticationFilter::getRequestMatcher)
                .collect(Collectors.toList()));
    }

    @Override
    public void configure(final HttpSecurity http) {
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        final AtomicReference<Class<? extends Filter>> afterFilter = new AtomicReference<>(FIRST_AFTER_FILTER);
        filters.forEach((filterClass, filter) -> {
            filter.setAuthenticationManager(authenticationManager);
            http.addFilterAfter(postProcess(filter), afterFilter.getAndSet(filterClass));
        });
    }

    SecurityFilterChain build(final HttpSecurity http) {
        try {
            return http.apply(this).and()
                    .securityMatcher(requestMatcher)
                    .csrf().ignoringRequestMatchers(requestMatcher).and()
                    .build();
        } catch (Exception e) {
            throw new PreAuthenticatedAuthenticationFilterConfigurerException("register filter failed.", e);
        }
    }
}
