package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationServiceManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.servlet.Filter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@AutoConfiguration
@EnableWebSecurity
public class PreAuthenticatedAuthenticationAutoConfiguration {
    /**
     * 装配PreAuthenticatedAuthenticationProvider.
     *
     * @param auths PreAuthenticatedAuthentication实例集合
     * @return PreAuthenticatedAuthenticationProvider实例
     */
    @Bean
    @ConditionalOnMissingBean(PreAuthenticatedAuthenticationProvider.class)
    PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(
            final List<PreAuthenticatedAuthentication<?, ?>> auths) {
        final PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setThrowExceptionWhenTokenRejected(true);
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedAuthenticationServiceManager(auths));
        return provider;
    }

    /**
     * 装配所有的{@link io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter}.
     *
     * @param http  HttpSecurity实例
     * @param auths PreAuthenticatedAuthentication实例集合
     * @return SecurityFilterChain
     * @throws Exception http.build()异常
     */
    @Bean
    SecurityFilterChain preAuthenticatedAuthenticationFilters(
            final HttpSecurity http, final List<PreAuthenticatedAuthentication<?, ?>> auths) throws Exception {
        return http.apply(new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
            @Override
            public void configure(final HttpSecurity builder) {
                final Set<Class<? extends Filter>> filterClasses = new HashSet<>();
                final AtomicReference<Class<? extends Filter>> afterFilter =
                        new AtomicReference<>(AbstractPreAuthenticatedProcessingFilter.class);
                final AuthenticationManager authManager = builder.getSharedObject(AuthenticationManager.class);
                auths.stream().map(auth -> auth.filter(authManager)).forEach(filter -> {
                    final Class<? extends Filter> filterClass = filter.getClass();
                    if (filterClasses.add(filterClass)) {
                        builder.addFilterAfter(filter, afterFilter.getAndSet(filterClass));
                    }
                });
            }
        }).and().build();
    }
}
