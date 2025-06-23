package org.yearup.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.yearup.dto.error.ErrorResponse;
import org.yearup.exception.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Handles custom application exceptions and formats API error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAuthErrors(Exception ex, HttpServletRequest request) {
        LOG.warn("Authentication error: {}", ex.getMessage());
        return buildResponseEntity(new UnauthorizedException("Authentication required"), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternal(InternalServerErrorException ex, HttpServletRequest request) {
        LOG.error("Internal server error", ex);
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return buildResponseEntity(new BadRequestException(message), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        LOG.error("Unhandled exception", ex);
        return buildResponseEntity(new InternalServerErrorException("Unexpected error"), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(ApplicationException ex, HttpStatus status, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
