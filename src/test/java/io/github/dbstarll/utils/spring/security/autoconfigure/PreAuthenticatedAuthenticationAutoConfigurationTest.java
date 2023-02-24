package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.AutowiredAuthentication;
import io.github.dbstarll.utils.spring.security.DirectAuthentication;
import io.github.dbstarll.utils.spring.security.autoconfigure.PreAuthenticatedAuthenticationAutoConfigurationTest.TestAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {
                ServletWebServerFactoryAutoConfiguration.class,
                PreAuthenticatedAuthenticationAutoConfiguration.class,
                TestAutoConfiguration.class
        })
class PreAuthenticatedAuthenticationAutoConfigurationTest {
    @Configuration
    static class TestAutoConfiguration {
        @Bean
        AutowiredAuthentication autowiredAuthentication() {
            return new AutowiredAuthentication();
        }

        @Bean
        DirectAuthentication directAuthentication() {
            return new DirectAuthentication();
        }
    }

    @Autowired(required = false)
    private PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider;

    @Test
    void preAuthenticatedAuthenticationProvider() {
        assertNotNull(preAuthenticatedAuthenticationProvider);
    }
}