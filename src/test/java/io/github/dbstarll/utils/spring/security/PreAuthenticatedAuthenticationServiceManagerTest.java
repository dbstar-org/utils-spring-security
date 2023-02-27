package io.github.dbstarll.utils.spring.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class PreAuthenticatedAuthenticationServiceManagerTest {
    private void useService(
            final Collection<PreAuthenticatedAuthentication<?, ?>> auths,
            final ThrowingConsumer<AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>> consumer)
            throws Throwable {
        consumer.accept(new PreAuthenticatedAuthenticationServiceManager(auths, new StaticApplicationContext()));
    }

    private PreAuthenticatedAuthenticationToken token(final Object principal, final Object credentials) {
        return new PreAuthenticatedAuthenticationToken(principal, credentials);
    }

    @Test
    void empty() throws Throwable {
        useService(Collections.emptyList(), s -> {
            final Exception e = assertThrowsExactly(ProviderNotFoundException.class,
                    () -> s.loadUserDetails(token("principal", "credentials")));
            assertEquals("PreAuthenticatedAuthentication<java.lang.String, java.lang.String> not found", e.getMessage());
        });
    }

    @Test
    void direct() throws Throwable {
        useService(Collections.singleton(new DirectAuthentication()), s -> {
            final UserDetails details = s.loadUserDetails(token("principal", "credentials"));
            assertNotNull(details);
            assertEquals("principal", details.getUsername());
            assertNull(details.getPassword());
        });
    }

    @Test
    void autowired() throws Throwable {
        useService(Collections.singleton(new AutowiredAuthentication()), s -> {
            final UserDetails details = s.loadUserDetails(token("principal", "credentials"));
            assertNotNull(details);
            assertEquals("principal", details.getUsername());
            assertNull(details.getPassword());
        });
    }
}