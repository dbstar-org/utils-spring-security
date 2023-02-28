package io.github.dbstarll.utils.spring.security.test;

import java.io.Serializable;
import java.util.StringJoiner;

public abstract class StringCredentials implements Serializable {
    private final String credentials;

    protected StringCredentials(String credentials) {
        this.credentials = credentials;
    }

    public final String getCredentials() {
        return credentials;
    }

    @Override
    public final String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("credentials='" + credentials + "'")
                .toString();
    }
}
