package io.github.dbstarll.utils.spring.security.test;

import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationService;
import io.github.dbstarll.utils.spring.security.PreAuthenticatedAuthenticationUserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

public class StringAuthenticationService implements PreAuthenticatedAuthenticationService {
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        final String principal = (String) token.getPrincipal();
        final StringCredentials credentials = (StringCredentials) token.getCredentials();
        if (!"principal".equals(principal)) {
            throw new UsernameNotFoundException(principal);
        } else if (!"credentials".equals(credentials.getCredentials())) {
            throw new BadCredentialsException("credentials error");
        } else {
            return new PreAuthenticatedAuthenticationUserDetails<String, StringCredentials>(token) {
                public String getInnerPrincipal() {
                    return getPrincipal();
                }

                public String getInnerCredentials() {
                    return getCredentials().getCredentials();
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.emptyList();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        }
    }
}
