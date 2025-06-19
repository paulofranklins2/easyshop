package org.yearup.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.yearup.model.User;

/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, CredentialsDto represents the data a client must
    pass to the server for a login endpoint, and TokenDto represents the object that's returned from the server
    to the client from a login endpoint.
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
