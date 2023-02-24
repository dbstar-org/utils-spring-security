package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.AutowiredAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Collections;
import java.util.HashMap;
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
                return object;
            }
        };
        final AuthenticationManagerBuilder authenticationBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        final Map<Class<?>, Object> sharedObjects = new HashMap<>();
        final AutowiredAuthentication authentication = new AutowiredAuthentication();
        authentication.setApplicationContext(new StaticApplicationContext());
        final Exception e = assertThrowsExactly(PreAuthenticatedAuthenticationFilterConfigurerException.class,
                () -> new PreAuthenticatedAuthenticationFilterConfigurerAdapter(Collections.singletonList(authentication))
                        .build(new HttpSecurity(objectPostProcessor, authenticationBuilder, sharedObjects)));
        assertNotNull(e.getCause());
        assertEquals(BeanCreationException.class, e.getCause().getClass());
        assertNotNull(e.getCause().getCause());
        assertEquals(IllegalArgumentException.class, e.getCause().getCause().getClass());
        assertEquals("An AuthenticationManager must be set", e.getCause().getCause().getMessage());
    }
}