package org.yearup.model.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data transfer object used for login requests.
 */
@Setter
@Getter
@ToString
public class LoginRequest {
    private String username;
    private String password;
}
