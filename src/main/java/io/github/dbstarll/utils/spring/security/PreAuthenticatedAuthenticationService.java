package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface PreAuthenticatedAuthenticationService<P, C> {
    /**
     * 根据认证token来载入认证用户详细信息.
     *
     * @param token 认证token
     * @return UserDetails 认证用户详细信息
     * @throws UsernameNotFoundException 无对应用户
     */
    UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken<P, C> token) throws UsernameNotFoundException;
}
