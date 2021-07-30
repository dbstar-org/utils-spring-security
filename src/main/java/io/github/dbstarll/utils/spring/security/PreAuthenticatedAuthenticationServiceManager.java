package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.lang.wrapper.EntryWrapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class PreAuthenticatedAuthenticationServiceManager
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final Map<Entry<?, ?>, PreAuthenticatedAuthentication<?, ?>> authentications;

    /**
     * 构造.
     *
     * @param authentications authentication集合
     */
    public PreAuthenticatedAuthenticationServiceManager(List<PreAuthenticatedAuthentication<?, ?>> authentications) {
        this.authentications = new HashMap<Entry<?, ?>, PreAuthenticatedAuthentication<?, ?>>();
        for (PreAuthenticatedAuthentication<?, ?> authentication : authentications) {
            this.authentications.put(parseKey(authentication), authentication);
        }
    }

    @SuppressWarnings("unchecked")
    private static <P, C> Entry<Class<P>, Class<C>> parseKey(PreAuthenticatedAuthentication<P, C> authentication) {
        final ParameterizedType type = (ParameterizedType) authentication.getClass().getGenericSuperclass();
        final Type[] keys = type.getActualTypeArguments();
        return EntryWrapper.wrap((Class<P>) keys[0], (Class<C>) keys[1]);
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return loadUserDetails(token.getPrincipal(), token.getCredentials(), token);
    }

    @SuppressWarnings("unchecked")
    private <P, C> UserDetails loadUserDetails(P principal, C credentials, PreAuthenticatedAuthenticationToken token) {
        final Entry<?, ?> key = EntryWrapper.wrap(principal.getClass(), credentials.getClass());
        final PreAuthenticatedAuthentication<P, C> authentication = (PreAuthenticatedAuthentication<P, C>) authentications
                .get(key);
        if (authentication != null) {
            return authentication.service().loadUserDetails(
                    new io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationToken<P, C>(token));
        } else {
            throw new UsernameNotFoundException(token.getName());
        }
    }
}
