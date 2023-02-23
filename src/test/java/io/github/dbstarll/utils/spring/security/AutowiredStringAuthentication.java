package io.github.dbstarll.utils.spring.security;

public abstract class AutowiredStringAuthentication extends AutowiredPreAuthenticatedAuthentication<String, String> {
    protected AutowiredStringAuthentication() {
        super(String.class, String.class);
    }
}
