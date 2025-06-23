package org.yearup.exception;

/**
 * Thrown when a request is malformed or fails validation.
 */
public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message);
    }
}
