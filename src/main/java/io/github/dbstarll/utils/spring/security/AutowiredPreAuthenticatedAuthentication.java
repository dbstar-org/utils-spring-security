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
    private final AtomicReference<PreAuthenticatedAuthenticationService<P, C>> refService;

    private AutowireCapableBeanFactory factory;

    protected AutowiredPreAuthenticatedAuthentication() {
        this.refFilter = new AtomicReference<PreAuthenticatedAuthenticationFilter<P, C>>();
        this.refService = new AtomicReference<PreAuthenticatedAuthenticationService<P, C>>();
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.factory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(factory, "AutowireCapableBeanFactory is null");
    }

    @Override
    public final PreAuthenticatedAuthenticationFilter<P, C> filter() {
        final PreAuthenticatedAuthenticationFilter<P, C> filter = refFilter.get();
        if (filter != null) {
            return filter;
        } else {
            final PreAuthenticatedAuthenticationFilter<P, C> original = notNull(originalFilter(), "filter is null");
            return refFilter.compareAndSet(null, original) ? autowire(original) : refFilter.get();
        }
    }

    @Override
    public final PreAuthenticatedAuthenticationService<P, C> service() {
        final PreAuthenticatedAuthenticationService<P, C> service = refService.get();
        if (service != null) {
            return service;
        } else {
            final PreAuthenticatedAuthenticationService<P, C> original = notNull(originalService(), "service is null");
            return refService.compareAndSet(null, original) ? autowire(original) : refService.get();
        }
    }

    private <I> I autowire(final I bean) {
        if (factory != null) {
            factory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            factory.initializeBean(bean, bean.getClass().getName());
        }
        return bean;
    }

    protected abstract PreAuthenticatedAuthenticationFilter<P, C> originalFilter();

    protected abstract PreAuthenticatedAuthenticationService<P, C> originalService();
}
