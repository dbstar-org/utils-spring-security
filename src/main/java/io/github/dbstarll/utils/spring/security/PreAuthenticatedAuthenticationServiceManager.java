package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.Validate.notNull;

public final class PreAuthenticatedAuthenticationServiceManager implements PreAuthenticatedAuthenticationService {
    private final Map<Entry<? extends Class<?>, ? extends Class<?>>, PreAuthenticatedAuthenticationService> services;

    /**
     * 构造.
     *
     * @param authentications authentication集合
     */
    public PreAuthenticatedAuthenticationServiceManager(
            final Iterable<PreAuthenticatedAuthentication<?, ?>> authentications) {
        this.services = new ConcurrentHashMap<>();
        authentications.forEach(auth -> this.services.put(
                parseKey(notNull(auth, "auth is null")),
                notNull(auth.service(), "auth.service() is null")));
    }

    private static <P, C> Entry<Class<P>, Class<C>> parseKey(final PreAuthenticatedAuthentication<P, C> auth) {
        return EntryWrapper.wrap(
                notNull(auth.getPrincipalClass(), "auth.getPrincipalClass() is null"),
                notNull(auth.getCredentialsClass(), "auth.getCredentialsClass() is null"));
    }

    @Override
    public UserDetails loadUserDetails(final PreAuthenticatedAuthenticationToken token)
            throws UsernameNotFoundException {
        final Entry<Class<?>, Class<?>> key = EntryWrapper.wrap(
                notNull(token.getPrincipal(), "token.getPrincipal() is null").getClass(),
                notNull(token.getCredentials(), "token.getCredentials() is null").getClass());
        return services.computeIfAbsent(key, k -> {
            final String msg = String.format("PreAuthenticatedAuthentication<%s, %s> not found",
                    k.getKey().getName(), k.getValue().getName());
            throw new UsernameNotFoundException(msg);
        }).loadUserDetails(token);
    }
}
