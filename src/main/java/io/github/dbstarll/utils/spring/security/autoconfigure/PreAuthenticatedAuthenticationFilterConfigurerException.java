package io.github.dbstarll.utils.spring.security.autoconfigure;

public class PreAuthenticatedAuthenticationFilterConfigurerException extends IllegalStateException {
    /**
     * 构造PreAuthenticatedAuthenticationFilterConfigurerException.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public PreAuthenticatedAuthenticationFilterConfigurerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
