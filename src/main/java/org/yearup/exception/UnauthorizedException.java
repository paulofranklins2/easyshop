package org.yearup.exception;

/**
 * Thrown when a user attempts an action without being authenticated.
 */
public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
