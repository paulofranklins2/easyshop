package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void roleManagement() {
        User user = new User();
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
        user.addRole("USER");
        assertTrue(user.getAuthorities().stream().anyMatch(a -> a.getName().equals("ROLE_USER")));
    }
}
