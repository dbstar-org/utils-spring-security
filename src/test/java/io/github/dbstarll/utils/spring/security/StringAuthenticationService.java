package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collections;

public class StringAuthenticationService implements PreAuthenticatedAuthenticationService {
    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return new User((String) token.getPrincipal(), (String) token.getCredentials(), Collections.emptyList());
    }
}
