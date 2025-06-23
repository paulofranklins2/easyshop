package org.yearup.exception;

/**
 * Thrown when an unexpected error occurs on the server.
 */
public class InternalServerErrorException extends ApplicationException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
