package io.github.dbstarll.utils.spring.security.autoconfigure;

import org.junit.jupiter.api.Test;

class PreAuthenticatedAuthenticationFilterConfigurerAdapterTest {
    @Test
    void build() {
//        final ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
//            @Override
//            public <O> O postProcess(O object) {
//                return object;
//            }
//        };
//        final AuthenticationManagerBuilder authenticationBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
//        final Map<Class<?>, Object> sharedObjects = new HashMap<>();
//        final AutowiredAuthentication authentication = new AutowiredAuthentication();
//        final Exception e = assertThrowsExactly(PreAuthenticatedAuthenticationFilterConfigurerException.class,
//                () -> PreAuthenticatedAuthenticationFilterConfigurerAdapter.build(
//                        new HttpSecurity(objectPostProcessor, authenticationBuilder, sharedObjects),
//                        Collections.singletonList(authentication)));
//        assertNotNull(e.getCause());
//        assertEquals(BeanCreationException.class, e.getCause().getClass());
//        assertNotNull(e.getCause().getCause());
//        assertEquals(IllegalArgumentException.class, e.getCause().getCause().getClass());
//        assertEquals("An AuthenticationManager must be set", e.getCause().getCause().getMessage());
    }
}