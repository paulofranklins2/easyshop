package org.yearup.security;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAccessDeniedHandlerTest {

    @Test
    void handle() throws IOException {
        JwtAccessDeniedHandler handler = new JwtAccessDeniedHandler();
        org.springframework.mock.web.MockHttpServletResponse res = new org.springframework.mock.web.MockHttpServletResponse();
        handler.handle(new org.springframework.mock.web.MockHttpServletRequest(), res,
            new org.springframework.security.access.AccessDeniedException("denied"));
        assertEquals(403, res.getStatus());
    }
}
