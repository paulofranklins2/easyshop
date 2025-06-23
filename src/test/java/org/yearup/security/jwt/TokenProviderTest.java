package org.yearup.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    @Test
    void tokenLifecycle() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());

        TokenProvider provider = new TokenProvider(base64Secret, 60);
        provider.afterPropertiesSet();

        var auth = new UsernamePasswordAuthenticationToken(
            "user",
            "p",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = provider.createToken(auth, false);

        assertNotNull(token);
        assertTrue(provider.validateToken(token));

        var auth2 = provider.getAuthentication(token);
        assertEquals("user", auth2.getName());
    }

    @Test
    void validateTokenReturnsFalseForInvalid() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String secret = Base64.getEncoder().encodeToString(key.getEncoded());
        TokenProvider provider = new TokenProvider(secret, 60);
        provider.afterPropertiesSet();

        assertFalse(provider.validateToken("bad.token"));
    }
}
