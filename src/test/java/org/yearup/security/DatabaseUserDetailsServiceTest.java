package org.yearup.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.yearup.exception.handler.UserNotActivatedException;
import org.yearup.model.User;
import org.yearup.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseUserDetailsServiceTest {

    @Test
    void loadUserByUsername() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        Mockito.when(repo.findByUsername("user")).thenReturn(new User("user", "p", "USER"));
        DatabaseUserDetailsService svc = new DatabaseUserDetailsService(repo);
        var details = svc.loadUserByUsername("user");
        assertEquals("user", details.getUsername());
    }

    @Test
    void loadUserByUsernameThrowsWhenNotActivated() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        User u = new User("user", "p", "USER");
        u.setActivated(false);
        Mockito.when(repo.findByUsername("user")).thenReturn(u);
        DatabaseUserDetailsService svc = new DatabaseUserDetailsService(repo);
        assertThrows(UserNotActivatedException.class, () -> svc.loadUserByUsername("user"));
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        Mockito.when(repo.findByUsername("missing")).thenReturn(null);
        DatabaseUserDetailsService svc = new DatabaseUserDetailsService(repo);
        assertThrows(UsernameNotFoundException.class,
            () -> svc.loadUserByUsername("missing"));
    }
}
