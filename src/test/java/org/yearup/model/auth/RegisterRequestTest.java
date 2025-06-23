package org.yearup.model.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterRequestTest {

    @Test
    void gettersAndSetters() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("bob");
        req.setPassword("p");
        req.setConfirmPassword("p");
        req.setRole("USER");

        assertEquals("bob", req.getUsername());
        assertEquals("p", req.getPassword());
        assertEquals("p", req.getConfirmPassword());
        assertEquals("USER", req.getRole());
    }
}
