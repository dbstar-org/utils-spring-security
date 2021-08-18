package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class PreAuthenticatedAuthenticationServiceManager
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final Map<Entry<?, ?>, PreAuthenticatedAuthentication<?, ?>> authentications;

    /**
     * 构造.
     *
     * @param authn authentication集合
     */
    public PreAuthenticatedAuthenticationServiceManager(final Collection<PreAuthenticatedAuthentication<?, ?>> authn) {
        this.authentications = new ConcurrentHashMap<>();
        authn.forEach(authentication -> this.authentications.put(parseKey(authentication), authentication));
    }

    @SuppressWarnings("unchecked")
    private static <P, C> Entry<Class<P>, Class<C>> parseKey(final PreAuthenticatedAuthentication<P, C> auth) {
        final ParameterizedType type = (ParameterizedType) auth.getClass().getGenericSuperclass();
        final Type[] keys = type.getActualTypeArguments();
        return EntryWrapper.wrap((Class<P>) keys[0], (Class<C>) keys[1]);
    }

    @Override
    public UserDetails loadUserDetails(final PreAuthenticatedAuthenticationToken token)
            throws UsernameNotFoundException {
        return loadUserDetails(token.getPrincipal(), token.getCredentials(), token);
    }

    @SuppressWarnings("unchecked")
    private <P, C> UserDetails loadUserDetails(final P principal,
                                               final C credentials,
                                               final PreAuthenticatedAuthenticationToken token) {
        final Entry<?, ?> key = EntryWrapper.wrap(principal.getClass(), credentials.getClass());
        return ((PreAuthenticatedAuthentication<P, C>) authentications.computeIfAbsent(key, k -> {
            throw new UsernameNotFoundException(token.getName());
        })).service().loadUserDetails(
                new io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationToken<>(token)
        );
    }
}
