package org.yearup.exception;

/**
 * Thrown when a resource cannot be located.
 */
public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message);
    }
}
