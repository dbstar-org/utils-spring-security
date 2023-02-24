package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationServiceManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.List;

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
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
     */
    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    SecurityFilterChain preAuthenticatedAuthenticationFilters(
            final HttpSecurity http, final List<PreAuthenticatedAuthentication<?, ?>> auths) {
        return new PreAuthenticatedAuthenticationFilterConfigurerAdapter(auths).build(http);
    }
}
