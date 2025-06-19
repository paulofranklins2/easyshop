package org.yearup.model.authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, LoginDto represents the data a
    client must pass to the server for a login endpoint, and LoginResponseDto represents the object that's returned
    from the server to the client from a login endpoint.
 */
@Setter
@Getter
@ToString
public class LoginDto {
    private String username;
    private String password;
}
