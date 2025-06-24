package org.yearup.exception.handler;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * Thrown when an attempt is made to authenticate a user that is not activated.
 */
public class UserNotActivatedException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = -1126699074574529145L;

    public UserNotActivatedException(String message) {
        super(message);
    }
}
