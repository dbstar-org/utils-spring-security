package io.github.dbstarll.utils.spring.security;

import org.springframework.context.ApplicationContext;

public final class PreAuthenticatedAuthenticationWrapper<P, C> extends AutowiredPreAuthenticatedAuthentication<P, C> {
    private final PreAuthenticatedAuthentication<P, C> original;

    private PreAuthenticatedAuthenticationWrapper(final PreAuthenticatedAuthentication<P, C> original) {
        super(original.getPrincipalClass(), original.getCredentialsClass());
        this.original = original;
    }

    @Override
    protected PreAuthenticatedAuthenticationFilter<P, C> originalFilter() {
        return original.filter();
    }

    @Override
    protected PreAuthenticatedAuthenticationService originalService() {
        return original.service();
    }

    /**
     * 封装一个PreAuthenticatedAuthentication.
     *
     * @param original           被封装的PreAuthenticatedAuthentication实例
     * @param applicationContext ApplicationContext
     * @param <P>                class of Principal
     * @param <C>                class of Credentials
     * @return 封装后的PreAuthenticatedAuthentication实例
     */
    public static <P, C> PreAuthenticatedAuthentication<P, C> wrap(
            final PreAuthenticatedAuthentication<P, C> original, final ApplicationContext applicationContext) {
        if (original instanceof AutowiredPreAuthenticatedAuthentication) {
            return original;
        } else {
            final PreAuthenticatedAuthenticationWrapper<P, C> wrapper =
                    new PreAuthenticatedAuthenticationWrapper<>(original);
            wrapper.setApplicationContext(applicationContext);
            return wrapper;
        }
    }
}
