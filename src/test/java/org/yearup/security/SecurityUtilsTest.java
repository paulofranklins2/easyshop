package org.yearup.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityUtilsTest {

    @Test
    void getCurrentUsername() {
        var auth = new UsernamePasswordAuthenticationToken("user", "pass");
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertEquals("user", SecurityUtils.getCurrentUsername().orElse(null));
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUsernameReturnsEmptyWhenNoAuth() {
        SecurityContextHolder.clearContext();
        assertTrue(SecurityUtils.getCurrentUsername().isEmpty());
    }
}
