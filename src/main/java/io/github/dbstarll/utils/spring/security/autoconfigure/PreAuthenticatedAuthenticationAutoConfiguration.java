package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.ExtendWebAuthenticationDetailsSource;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationServiceManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
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
     * @param ctx   ApplicationContext
     * @return PreAuthenticatedAuthenticationProvider实例
     */
    @Bean
    @ConditionalOnMissingBean(PreAuthenticatedAuthenticationProvider.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(
            final List<PreAuthenticatedAuthentication<?, ?>> auths, final ApplicationContext ctx) {
        final PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setThrowExceptionWhenTokenRejected(true);
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedAuthenticationServiceManager(auths, ctx));
        return provider;
    }

    /**
     * 装配所有的{@link io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationFilter}.
     *
     * @param http  HttpSecurity实例
     * @param auths PreAuthenticatedAuthentication实例集合
     * @param ctx   ApplicationContext
     * @return SecurityFilterChain
     */
    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    SecurityFilterChain preAuthenticatedAuthenticationFilters(final HttpSecurity http,
                                                              final List<PreAuthenticatedAuthentication<?, ?>> auths,
                                                              final ApplicationContext ctx) {
        return new PreAuthenticatedAuthenticationFilterConfigurerAdapter(auths, ctx).build(http);
    }

    /**
     * 注入ExtendWebAuthenticationDetailsSource实例.
     *
     * @return ExtendWebAuthenticationDetailsSource实例
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationDetailsSource.class)
    ExtendWebAuthenticationDetailsSource extendWebAuthenticationDetailsSource() {
        return new ExtendWebAuthenticationDetailsSource();
    }
}
