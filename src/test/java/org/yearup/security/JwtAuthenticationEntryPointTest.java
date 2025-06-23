package org.yearup.security;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationEntryPointTest {

    @Test
    void commence() throws IOException {
        JwtAuthenticationEntryPoint entry = new JwtAuthenticationEntryPoint();
        org.springframework.mock.web.MockHttpServletResponse res = new org.springframework.mock.web.MockHttpServletResponse();
        entry.commence(new org.springframework.mock.web.MockHttpServletRequest(), res,
            new org.springframework.security.core.AuthenticationException("err"){});
        assertEquals(401, res.getStatus());
    }
}
