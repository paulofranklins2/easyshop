package org.yearup.security.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    @Test
    void tokenLifecycle() {
        String secret = java.util.Base64.getEncoder().encodeToString("secret".getBytes());
        TokenProvider provider = new TokenProvider(secret, 60);
        provider.afterPropertiesSet();
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("user", "p", java.util.List.of());
        String token = provider.createToken(auth, false);
        assertNotNull(token);
        assertTrue(provider.validateToken(token));
        var auth2 = provider.getAuthentication(token);
        assertEquals("user", auth2.getName());
    }
}
