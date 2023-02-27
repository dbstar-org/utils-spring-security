package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.AutowiredAuthentication;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class PreAuthenticatedAuthenticationFilterConfigurerAdapterTest {
    @Test
    void build() {
        final ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
            @Override
            public <O> O postProcess(O object) {
                throw new IllegalArgumentException("postProcess failed.");
            }
        };
        final AuthenticationManagerBuilder authenticationBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        final Map<Class<?>, Object> sharedObjects = new HashMap<>();
        final HttpSecurity http = new HttpSecurity(objectPostProcessor, authenticationBuilder, sharedObjects);
        final ApplicationContext context = new StaticApplicationContext();

        final AutowiredAuthentication authentication = new AutowiredAuthentication();
        final List<PreAuthenticatedAuthentication<?, ?>> authentications = Collections.singletonList(authentication);

        final Exception e = assertThrowsExactly(PreAuthenticatedAuthenticationFilterConfigurerException.class,
                () -> new PreAuthenticatedAuthenticationFilterConfigurerAdapter(authentications, context).build(http));
        assertEquals("register filter failed.", e.getMessage());
        assertNotNull(e.getCause());
        assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        assertEquals("postProcess failed.", e.getCause().getMessage());
    }
}