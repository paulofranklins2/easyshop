package org.yearup.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebSecurityConfigTest {

    @Test
    void passwordEncoder() {
        WebSecurityConfig config = new WebSecurityConfig(null, null, null, null);
        assertNotNull(config.passwordEncoder().encode("pass"));
    }
}
