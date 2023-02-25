package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

final class PreAuthenticatedAuthenticationFilterConfigurerAdapter
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private static final Class<? extends Filter> FIRST_AFTER_FILTER = AbstractPreAuthenticatedProcessingFilter.class;

    private final List<PreAuthenticatedAuthenticationFilter<?, ?>> filters;
    private final RequestMatcher requestMatcher;

    PreAuthenticatedAuthenticationFilterConfigurerAdapter(final List<PreAuthenticatedAuthentication<?, ?>> auths) {
        final Set<Class<?>> distinctFilterClasses = new HashSet<>();
        this.filters = auths.stream()
                .map(PreAuthenticatedAuthentication::filter)
                .filter(filter -> distinctFilterClasses.add(filter.getClass()))
                .collect(Collectors.toList());
        this.requestMatcher = new OrRequestMatcher(filters.stream()
                .map(PreAuthenticatedAuthenticationFilter::getRequestMatcher)
                .collect(Collectors.toList()));
    }

    @Override
    public void configure(final HttpSecurity http) {
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        final AtomicReference<Class<? extends Filter>> afterFilter = new AtomicReference<>(FIRST_AFTER_FILTER);
        filters.forEach(filter -> {
            filter.setAuthenticationManager(authenticationManager);
            http.addFilterAfter(postProcess(filter), afterFilter.getAndSet(filter.getClass()));
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
