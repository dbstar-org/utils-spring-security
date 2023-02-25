package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.spring.security.ExtendWebAuthenticationDetailsSource.ExtendWebAuthenticationDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ExtendWebAuthenticationDetailsSourceTest {
    private ExtendWebAuthenticationDetailsSource source;

    @BeforeEach
    void setUp() {
        source = new ExtendWebAuthenticationDetailsSource();
    }

    @AfterEach
    void tearDown() {
        source = null;
    }

    @Test
    void buildDetails() {
        final ExtendWebAuthenticationDetails details = source.buildDetails(MockMvcRequestBuilders
                .get("/auth")
                .header("user-agent", "ExtendWebAuthenticationDetailsSourceTest")
                .buildRequest(null));
        assertEquals("ExtendWebAuthenticationDetailsSourceTest", details.getUserAgent());

        final ExtendWebAuthenticationDetails details1 = source.buildDetails(MockMvcRequestBuilders
                .get("/auth")
                .header("user-agent", "ExtendWebAuthenticationDetailsSourceTest")
                .buildRequest(null));

        assertNotEquals(details, null);
        assertNotEquals(details, new Object());
        assertEquals(details, details);
        assertEquals(details, details1);
        assertEquals(details.hashCode(), details1.hashCode());
    }
}