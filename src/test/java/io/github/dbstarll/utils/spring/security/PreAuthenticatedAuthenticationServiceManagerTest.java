package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import io.github.dbstarll.utils.spring.security.test.AutowiredAuthentication;
import io.github.dbstarll.utils.spring.security.test.DirectAuthentication;
import io.github.dbstarll.utils.spring.security.test.DirectPostAuthentication;
import io.github.dbstarll.utils.spring.security.test.GetCredentials;
import io.github.dbstarll.utils.spring.security.test.PostCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PreAuthenticatedAuthenticationServiceManagerTest {
    private void useService(
            final Collection<PreAuthenticatedAuthentication<?, ?>> auths,
            final ThrowingConsumer<PreAuthenticatedAuthenticationService<?, ?>> consumer)
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
            final Entry<Class<?>, Class<?>> key = EntryWrapper.wrap(String.class, String.class);
            assertEquals(PreAuthenticatedAuthenticationServiceManager.name(key) + " not found", e.getMessage());
        });
    }

    @Test
    void direct() throws Throwable {
        useService(Collections.singleton(new DirectAuthentication()), s -> {
            final UserDetails details = s.loadUserDetails(token("principal", new GetCredentials("credentials")));
            assertNotNull(details);
            assertEquals("principal", details.getUsername());
            assertNull(details.getPassword());
        });
    }

    @Test
    void autowired() throws Throwable {
        useService(Collections.singleton(new AutowiredAuthentication()), s -> {
            final UserDetails details = s.loadUserDetails(token("principal", new PostCredentials("credentials")));
            assertNotNull(details);
            assertEquals("principal", details.getUsername());
            assertNull(details.getPassword());
        });
    }

    @Test
    void duplicate() {
        final Exception e = assertThrowsExactly(IllegalArgumentException.class, () -> useService(
                Arrays.asList(new AutowiredAuthentication(), new DirectPostAuthentication()),
                s -> fail("mush throw IllegalArgumentException")));
        final Entry<Class<?>, Class<?>> key = EntryWrapper.wrap(String.class, PostCredentials.class);
        assertTrue(e.getMessage().startsWith("Duplicate " + PreAuthenticatedAuthenticationServiceManager.name(key)));
    }
}