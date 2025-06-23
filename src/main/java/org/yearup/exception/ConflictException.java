package org.yearup.exception;

/**
 * Thrown when a request conflicts with existing state of the system.
 */
public class ConflictException extends ApplicationException {
    public ConflictException(String message) {
        super(message);
    }
}
