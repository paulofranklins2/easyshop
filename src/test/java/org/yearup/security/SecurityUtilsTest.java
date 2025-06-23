package org.yearup.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @Test
    void getCurrentUsername() {
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("user", "pass");
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
        assertEquals("user", SecurityUtils.getCurrentUsername().orElse(null));
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}
