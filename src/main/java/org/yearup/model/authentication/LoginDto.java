package org.yearup.model.authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data transfer object used for login requests.
 */
@Setter
@Getter
@ToString
public class LoginDto {
    private String username;
    private String password;
}