package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.spring.security.test.AutowiredAuthentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class AutowiredPreAuthenticatedAuthenticationTest {
    private AutowiredAuthentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new AutowiredAuthentication();
    }

    @AfterEach
    void tearDown() {
        authentication = null;
    }

    @Test
    void filter() {
        final PreAuthenticatedAuthenticationFilter<?, ?> filter = authentication.filter();
        assertNotNull(filter);
        final PreAuthenticatedAuthenticationFilter<?, ?> again = authentication.filter();
        assertNotNull(again);
        assertSame(filter, again);
    }

    @Test
    void service() {
        final PreAuthenticatedAuthenticationService<?, ?> service = authentication.service();
        assertNotNull(service);
        final PreAuthenticatedAuthenticationService<?, ?> again = authentication.service();
        assertNotNull(again);
        assertSame(service, again);
    }

    @Test
    void autowire() {
        try (final GenericApplicationContext ctx = new StaticApplicationContext()) {
            authentication.setApplicationContext(ctx);
            authentication.afterPropertiesSet();
            assertNotNull(authentication.service());
        }
    }
}