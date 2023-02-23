package io.github.dbstarll.utils.spring.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class StringAuthenticationUserDetails extends PreAuthenticatedAuthenticationUserDetails<String, String> {
    public StringAuthenticationUserDetails(PreAuthenticatedAuthenticationToken<String, String> token) {
        super(token);
    }

    @Override
    public String getPassword() {
        return getToken().getCredentials();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
}
