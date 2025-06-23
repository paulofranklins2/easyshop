package org.yearup.exception.handler;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.yearup.dto.error.ErrorResponse;
import org.yearup.exception.BadRequestException;
import org.yearup.exception.InternalServerErrorException;
import org.yearup.exception.NotFoundException;

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

    @Test
    void handleAccessDeniedReturns403() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        org.springframework.security.access.AccessDeniedException ex =
            new org.springframework.security.access.AccessDeniedException("denied");
        ResponseEntity<ErrorResponse> response = handler.handleAccessDenied(ex, new MockHttpServletRequest());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(403, response.getBody().getStatus());
        assertEquals("Access denied", response.getBody().getMessage());
    }

    @Test
    void handleBadRequestReturns400() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        BadRequestException ex = new BadRequestException("bad");
        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(ex, new MockHttpServletRequest());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("bad", response.getBody().getMessage());
    }

    @Test
    void handleNotFoundReturns404() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        NotFoundException ex = new NotFoundException("missing");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex, new MockHttpServletRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("missing", response.getBody().getMessage());
    }

    @Test
    void handleValidationAggregatesMessages() throws NoSuchMethodException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Object target = new Object();
        org.springframework.validation.BeanPropertyBindingResult br =
            new org.springframework.validation.BeanPropertyBindingResult(target, "t");
        br.addError(new FieldError("t", "f1", "m1"));
        br.addError(new FieldError("t", "f2", "m2"));
        MethodParameter param = new MethodParameter(String.class.getMethods()[0], -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(param, br);

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex, new MockHttpServletRequest());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("m1, m2", response.getBody().getMessage());
    }

    @Test
    void handleInternalAndHandleAllReturn500() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        InternalServerErrorException ex = new InternalServerErrorException("err", new RuntimeException());
        ResponseEntity<ErrorResponse> response = handler.handleInternal(ex, new MockHttpServletRequest());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ResponseEntity<ErrorResponse> response2 = handler.handleAll(new RuntimeException("boom"), new MockHttpServletRequest());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response2.getStatusCode());
    }
}
