package org.yearup.security;

import org.junit.jupiter.api.Test;
import org.yearup.model.User;
import org.yearup.security.UserNotActivatedException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserDetailsServiceTest {

    @Test
    void loadUserByUsername() {
        org.yearup.repository.UserRepository repo = org.mockito.Mockito.mock(org.yearup.repository.UserRepository.class);
        org.mockito.Mockito.when(repo.findByUsername("user")).thenReturn(new User("user","p","USER"));
        DatabaseUserDetailsService svc = new DatabaseUserDetailsService(repo);
        var details = svc.loadUserByUsername("user");
        assertEquals("user", details.getUsername());
    }

    @Test
    void loadUserByUsernameThrowsWhenNotActivated() {
        org.yearup.repository.UserRepository repo = org.mockito.Mockito.mock(org.yearup.repository.UserRepository.class);
        User u = new User("user","p","USER");
        u.setActivated(false);
        org.mockito.Mockito.when(repo.findByUsername("user")).thenReturn(u);
        DatabaseUserDetailsService svc = new DatabaseUserDetailsService(repo);
        assertThrows(UserNotActivatedException.class, () -> svc.loadUserByUsername("user"));
    }
}