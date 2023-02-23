package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class StringAuthenticationService implements PreAuthenticatedAuthenticationService<String, String> {
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken<String, String> token) throws UsernameNotFoundException {
        return new StringAuthenticationUserDetails(token);
    }
}
