package io.github.dbstarll.utils.spring.security.autoconfigure;

import io.github.dbstarll.utils.spring.security.AuthPostAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.AutowiredAuthentication;
import io.github.dbstarll.utils.spring.security.DirectAuthentication;
import io.github.dbstarll.utils.spring.security.autoconfigure.PreAuthenticatedAuthenticationAutoConfigurationTest.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {
                ServletWebServerFactoryAutoConfiguration.class,
                PreAuthenticatedAuthenticationAutoConfiguration.class,
                TestConfiguration.class
        })
class PreAuthenticatedAuthenticationAutoConfigurationTest {
    @SpringBootApplication
    static class TestConfiguration {
        @Bean
        AutowiredAuthentication autowiredAuthentication() {
            return new AutowiredAuthentication();
        }

        @Bean
        DirectAuthentication directAuthentication() {
            return new DirectAuthentication();
        }

        @RestController
        static class HomeController {
            @RequestMapping("/auth")
            public Authentication auth(final Authentication authentication) {
                return authentication;
            }
        }
    }

    @Autowired(required = false)
    private PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider;

    @Test
    void preAuthenticatedAuthenticationProvider() {
        assertNotNull(preAuthenticatedAuthenticationProvider);
    }

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url() {
        return "http://localhost:" + port + "/auth";
    }

    @Test
    void noAuthentication() {
        assertNull(restTemplate.getForEntity(url(), String.class).getBody());
    }

    @Test
    void authentication() {
        final ResponseEntity<String> entity = restTemplate.exchange(url(), HttpMethod.POST,
                authEntity(), String.class);
        assertNotNull(entity.getBody());
        System.out.println(entity.getBody());
        entity.getHeaders().entrySet().forEach(System.out::println);
    }

    @Test
    void filterNotMatch() {
        assertNull(restTemplate.exchange(url(), HttpMethod.GET, authEntity(), String.class).getBody());
    }

    private HttpEntity<String> authEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(AuthPostAuthenticationFilter.HEADER_PRINCIPAL, "principal");
        headers.add(AuthPostAuthenticationFilter.HEADER_CREDENTIALS, "credentials");
        return new HttpEntity<>(headers);
    }
}