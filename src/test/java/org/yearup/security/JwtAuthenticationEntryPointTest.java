package org.yearup.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationEntryPointTest {

    @Test
    void commence() throws IOException {
        JwtAuthenticationEntryPoint entry = new JwtAuthenticationEntryPoint();
        MockHttpServletResponse res = new MockHttpServletResponse();
        entry.commence(new MockHttpServletRequest(), res,
            new AuthenticationException("err"){});
        assertEquals(401, res.getStatus());
    }
}
