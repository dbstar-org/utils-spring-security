package io.github.dbstarll.utils.spring.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class AutowiredPreAuthenticatedAuthentication<P, C>
        implements PreAuthenticatedAuthentication<P, C>, ApplicationContextAware, InitializingBean {
    private final AtomicReference<PreAuthenticatedAuthenticationFilter<P, C>> refFilter;
    private final AtomicReference<PreAuthenticatedAuthenticationService> refService;

    private final Class<P> principalClass;
    private final Class<C> credentialsClass;

    private AutowireCapableBeanFactory factory;

    protected AutowiredPreAuthenticatedAuthentication(final Class<P> principalClass, final Class<C> credentialsClass) {
        this.refFilter = new AtomicReference<>();
        this.refService = new AtomicReference<>();
        this.principalClass = principalClass;
        this.credentialsClass = credentialsClass;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.factory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    public void afterPropertiesSet() {
        notNull(factory, "AutowireCapableBeanFactory is null");
    }

    @Override
    public final PreAuthenticatedAuthenticationFilter<P, C> filter() {
        final PreAuthenticatedAuthenticationFilter<P, C> filter = refFilter.get();
        if (filter != null) {
            return filter;
        } else {
            refFilter.compareAndSet(null, autowire(notNull(originalFilter(), "originalFilter is null")));
            return refFilter.get();
        }
    }

    @Override
    public final PreAuthenticatedAuthenticationService service() {
        final PreAuthenticatedAuthenticationService service = refService.get();
        if (service != null) {
            return service;
        } else {
            refService.compareAndSet(null, autowire(notNull(originalService(), "originalService is null")));
            return refService.get();
        }
    }

    private <I> I autowire(final I bean) {
        if (factory != null) {
            factory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        }
        return bean;
    }

    protected abstract PreAuthenticatedAuthenticationFilter<P, C> originalFilter();

    protected abstract PreAuthenticatedAuthenticationService originalService();

    @Override
    public final Class<P> getPrincipalClass() {
        return principalClass;
    }

    @Override
    public final Class<C> getCredentialsClass() {
        return credentialsClass;
    }
}
