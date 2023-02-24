package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.Filter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

class PreAuthenticatedAuthenticationFilterConfigurerAdapter
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final List<PreAuthenticatedAuthentication<?, ?>> auths;

    PreAuthenticatedAuthenticationFilterConfigurerAdapter(final List<PreAuthenticatedAuthentication<?, ?>> auths) {
        this.auths = auths;
    }

    @Override
    public void configure(final HttpSecurity http) {
        final Set<Class<? extends Filter>> filterClasses = new HashSet<>();
        final AtomicReference<Class<? extends Filter>> afterFilter =
                new AtomicReference<>(AbstractPreAuthenticatedProcessingFilter.class);
        final AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);
        auths.stream().map(auth -> auth.filter(authManager)).forEach(filter -> {
            final Class<? extends Filter> filterClass = filter.getClass();
            if (filterClasses.add(filterClass)) {
                http.addFilterAfter(filter, afterFilter.getAndSet(filterClass));
            }
        });
    }

    SecurityFilterChain build(final HttpSecurity http) {
        try {
            return http.apply(this).and().build();
        } catch (Exception e) {
            throw new PreAuthenticatedAuthenticationFilterConfigurerException("register filter failed.", e);
        }
    }
}
