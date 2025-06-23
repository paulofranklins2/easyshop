package org.yearup.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorityTest {

    @Test
    void basic() {
        Authority a1 = new Authority("ROLE_USER");
        Authority a2 = new Authority("ROLE_USER");
        assertEquals(a1, a2);
        a2.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", a2.getName());
        assertTrue(a2.toString().contains("ROLE_ADMIN"));
    }
}
