package org.yearup.model.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void gettersAndSetters() {
        LoginRequest req = new LoginRequest();
        req.setUsername("u");
        req.setPassword("p");
        assertEquals("u", req.getUsername());
        assertEquals("p", req.getPassword());
        assertTrue(req.toString().contains("u"));
    }
}
