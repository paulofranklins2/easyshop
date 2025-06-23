package org.yearup.exception;

/**
 * Thrown when an authenticated user attempts an action they do not have
 * permission to perform.
 */
public class ForbiddenException extends ApplicationException {
    public ForbiddenException(String message) {
        super(message);
    }
}
