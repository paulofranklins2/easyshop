package org.yearup.exception.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.yearup.dto.error.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void handleAuthErrorsReturns401() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        AuthenticationException ex = new AuthenticationException("fail") {};
        ResponseEntity<ErrorResponse> response = handler.handleAuthErrors(ex, new MockHttpServletRequest());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().getStatus());
        assertEquals("Authentication required", response.getBody().getMessage());
    }
}
