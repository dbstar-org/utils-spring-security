package io.github.dbstarll.utils.spring.security.autoconfigure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.utils.spring.security.autoconfigure.PreAuthenticatedAuthenticationAutoConfigurationTest.TestConfiguration;
import io.github.dbstarll.utils.spring.security.test.AuthGetAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.test.AuthPostAuthenticationFilter;
import io.github.dbstarll.utils.spring.security.test.AutowiredAuthentication;
import io.github.dbstarll.utils.spring.security.test.DirectAuthentication;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    private String url() {
        return "http://localhost:" + port + "/auth";
    }

    @Test
    void noAuthentication() {
        assertNull(restTemplate.getForEntity(url(), String.class).getBody());
    }

    @Test
    void authenticationPost() throws JsonProcessingException {
        final ResponseEntity<String> entity = restTemplate.exchange(url(), HttpMethod.POST,
                authPostEntity(), String.class);
        assertNotNull(entity.getBody());
        final JsonNode node = objectMapper.readTree(entity.getBody());
        assertTrue(node.get("authenticated").asBoolean());
        assertEquals("authPostEntity", node.at("/details/userAgent").asText());
        assertEquals("POST", node.at("/details/method").asText());
        assertEquals("http", node.at("/details/scheme").asText());
        assertEquals("localhost", node.at("/details/host").asText());
        assertEquals(port, node.at("/details/port").asInt());
        assertEquals("/auth", node.at("/details/uri").asText());
        assertEquals("credentials", node.at("/credentials/credentials").asText());
        assertEquals("principal", node.get("name").asText());
        assertEquals("principal", node.at("/principal/username").asText());
        assertTrue(node.at("/principal/enabled").asBoolean());
        assertTrue(entity.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
    }

    @Test
    void authenticationGet() throws JsonProcessingException {
        final ResponseEntity<String> entity = restTemplate.exchange(url(), HttpMethod.GET,
                authGetEntity(), String.class);
        assertNotNull(entity.getBody());
        final JsonNode node = objectMapper.readTree(entity.getBody());
        assertTrue(node.get("authenticated").asBoolean());
        assertEquals("authGetEntity", node.at("/details/userAgent").asText());
        assertEquals("GET", node.at("/details/method").asText());
        assertEquals("http", node.at("/details/scheme").asText());
        assertEquals("localhost", node.at("/details/host").asText());
        assertEquals(port, node.at("/details/port").asInt());
        assertEquals("/auth", node.at("/details/uri").asText());
        assertEquals("credentials", node.at("/credentials/credentials").asText());
        assertEquals("principal", node.get("name").asText());
        assertEquals("principal", node.at("/principal/username").asText());
        assertTrue(node.at("/principal/enabled").asBoolean());
        assertTrue(entity.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
    }

    @Test
    void filterNotMatch() {
        assertNull(restTemplate.exchange(url(), HttpMethod.GET, authPostEntity(), String.class).getBody());
    }

    private HttpEntity<String> authPostEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(AuthPostAuthenticationFilter.HEADER_PRINCIPAL, "principal");
        headers.add(AuthPostAuthenticationFilter.HEADER_CREDENTIALS, "credentials");
        headers.add(HttpHeaders.USER_AGENT, "authPostEntity");
        return new HttpEntity<>(headers);
    }

    private HttpEntity<String> authGetEntity() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(AuthGetAuthenticationFilter.HEADER_PRINCIPAL, "principal");
        headers.add(AuthGetAuthenticationFilter.HEADER_CREDENTIALS, "credentials");
        headers.add(HttpHeaders.USER_AGENT, "authGetEntity");
        return new HttpEntity<>(headers);
    }
}