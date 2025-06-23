package org.yearup.security.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtFilterTest {

    @Test
    void doFilter() throws Exception {
        TokenProvider provider = org.mockito.Mockito.mock(TokenProvider.class);
        org.mockito.Mockito.when(provider.validateToken("t")).thenReturn(true);
        org.springframework.security.core.Authentication auth =
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("u", "p");
        org.mockito.Mockito.when(provider.getAuthentication("t")).thenReturn(auth);

        JwtFilter filter = new JwtFilter(provider);
        org.springframework.mock.web.MockHttpServletRequest req = new org.springframework.mock.web.MockHttpServletRequest();
        req.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer t");
        org.springframework.mock.web.MockHttpServletResponse res = new org.springframework.mock.web.MockHttpServletResponse();
        filter.doFilter(req, res, (r, s) -> {});

        assertEquals("u", org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName());
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}
