package org.yearup.model.auth;

import org.junit.jupiter.api.Test;
import org.yearup.model.User;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void gettersAndSetters() {
        User user = new User();
        LoginResponse res = new LoginResponse("abc", user);
        assertEquals("abc", res.getToken());
        assertEquals(user, res.getUser());
    }
}
