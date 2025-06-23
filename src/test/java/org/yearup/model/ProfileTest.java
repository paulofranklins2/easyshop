package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfileTest {

    @Test
    void gettersAndSetters() {
        Profile p = new Profile();
        p.setUserId(1);
        p.setFirstName("f");
        p.setLastName("l");
        p.setPhone("p");
        p.setEmail("e");
        p.setAddress("a");
        p.setCity("c");
        p.setState("s");
        p.setZip("z");
        p.setPhotoUrl("ph");
        p.setProfileUrl("pr");

        assertEquals(1, p.getUserId());
        assertEquals("f", p.getFirstName());
        assertEquals("l", p.getLastName());
        assertEquals("p", p.getPhone());
        assertEquals("e", p.getEmail());
        assertEquals("a", p.getAddress());
        assertEquals("c", p.getCity());
        assertEquals("s", p.getState());
        assertEquals("z", p.getZip());
        assertEquals("ph", p.getPhotoUrl());
        assertEquals("pr", p.getProfileUrl());
    }
}
