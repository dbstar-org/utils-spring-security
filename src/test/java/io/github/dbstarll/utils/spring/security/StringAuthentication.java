package io.github.dbstarll.utils.spring.security;

public interface StringAuthentication extends PreAuthenticatedAuthentication<String, String> {
    @Override
    default Class<String> getPrincipalClass() {
        return String.class;
    }

    @Override
    default Class<String> getCredentialsClass() {
        return String.class;
    }
}
