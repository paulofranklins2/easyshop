package org.yearup.security.jwt;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {

    @Test
    void doFilter() throws Exception {
        TokenProvider provider = Mockito.mock(TokenProvider.class);
        Mockito.when(provider.validateToken("t")).thenReturn(true);
        Authentication auth =
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("u", "p");
        Mockito.when(provider.getAuthentication("t")).thenReturn(auth);

        JwtFilter filter = new JwtFilter(provider);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer t");
        MockHttpServletResponse res = new MockHttpServletResponse();
        filter.doFilter(req, res, (r, s) -> {});

        assertEquals("u", SecurityContextHolder.getContext().getAuthentication().getName());
        SecurityContextHolder.clearContext();
    }
}
