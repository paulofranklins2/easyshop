package org.yearup.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.yearup.model.User;

/**
 * Response returned after a successful login containing the JWT token and user details.
 */
@Setter
@AllArgsConstructor
@Getter
public class LoginResponseDto {

    @JsonProperty("token")
    private String token;
    @JsonProperty("user")
    private User user;
}