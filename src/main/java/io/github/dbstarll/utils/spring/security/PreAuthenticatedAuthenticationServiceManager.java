package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.Validate.notNull;

public final class PreAuthenticatedAuthenticationServiceManager implements PreAuthenticatedAuthenticationService {
    private final Map<Entry<Class<?>, Class<?>>, PreAuthenticatedAuthenticationService> services;

    /**
     * 构造.
     *
     * @param authentications authentication集合
     * @param ctx             ApplicationContext
     */
    public PreAuthenticatedAuthenticationServiceManager(
            final Collection<PreAuthenticatedAuthentication<?, ?>> authentications, final ApplicationContext ctx) {
        this.services = new ConcurrentHashMap<>();
        authentications.stream()
                .map(auth -> PreAuthenticatedAuthenticationWrapper.wrap(auth, ctx))
                .forEach(auth -> this.services.compute(parseKey(auth), (key, old) -> {
                    if (old == null) {
                        return auth.service();
                    } else {
                        //TODO 重复警告
                        return old;
                    }
                }));
    }

    private static Entry<Class<?>, Class<?>> parseKey(final PreAuthenticatedAuthentication<?, ?> authentication) {
        return EntryWrapper.wrap(
                notNull(authentication.getPrincipalClass(), "authentication.getPrincipalClass() is null"),
                notNull(authentication.getCredentialsClass(), "authentication.getCredentialsClass() is null"));
    }

    private static Entry<Class<?>, Class<?>> parseKey(final PreAuthenticatedAuthenticationToken token) {
        return EntryWrapper.wrap(
                notNull(token.getPrincipal(), "token.getPrincipal() is null").getClass(),
                notNull(token.getCredentials(), "token.getCredentials() is null").getClass());
    }

    @Override
    public UserDetails loadUserDetails(final PreAuthenticatedAuthenticationToken token)
            throws UsernameNotFoundException {
        return services.computeIfAbsent(parseKey(token), k -> {
            final String msg = String.format("PreAuthenticatedAuthentication<%s, %s> not found",
                    k.getKey().getName(), k.getValue().getName());
            throw new ProviderNotFoundException(msg);
        }).loadUserDetails(token);
    }
}
