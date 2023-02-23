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
    private final Map<Entry<?, ?>, PreAuthenticatedAuthenticationService> services;

    /**
     * 构造.
     *
     * @param authentications authentication集合
     */
    public PreAuthenticatedAuthenticationServiceManager(
            final Iterable<PreAuthenticatedAuthentication<?, ?>> authentications) {
        this.services = new ConcurrentHashMap<>();
        authentications.forEach(auth -> this.services.put(parseKey(notNull(auth)), notNull(auth.service())));
    }

    private static <P, C> Entry<Class<P>, Class<C>> parseKey(final PreAuthenticatedAuthentication<P, C> auth) {
        return EntryWrapper.wrap(notNull(auth.getPrincipalClass()), notNull(auth.getCredentialsClass()));
    }

    @Override
    public UserDetails loadUserDetails(final PreAuthenticatedAuthenticationToken token)
            throws UsernameNotFoundException {
        final Entry<?, ?> key = EntryWrapper.wrap(token.getPrincipal().getClass(), token.getCredentials().getClass());
        return services.computeIfAbsent(key, k -> {
            throw new UsernameNotFoundException(token.getName());
        }).loadUserDetails(token);
    }
}
