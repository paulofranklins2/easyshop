package org.yearup.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JwtAccessDeniedHandlerTest {

    @Test
    void handle() throws IOException {
        JwtAccessDeniedHandler handler = new JwtAccessDeniedHandler();
        MockHttpServletResponse res = new MockHttpServletResponse();
        handler.handle(new MockHttpServletRequest(), res,
            new AccessDeniedException("denied"));
        assertEquals(403, res.getStatus());
    }
}
