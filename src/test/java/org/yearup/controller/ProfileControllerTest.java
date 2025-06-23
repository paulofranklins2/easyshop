package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.yearup.model.Profile;
import org.yearup.model.User;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfileControllerTest {

    @Test
    void getProfile() {
        ProfileRepository repo = mock(ProfileRepository.class);
        UserRepository userRepo = mock(UserRepository.class);
        ProfileController controller = new ProfileController(repo, userRepo);

        // Arrange mock user and profile
        User user = new User(); user.setId(1);
        when(userRepo.findByUsername("u")).thenReturn(user);
        when(repo.findById(1)).thenReturn(java.util.Optional.of(new Profile()));

        // Set security context properly
        var auth = new UsernamePasswordAuthenticationToken("u", "p", List.of());
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

        // Act + Assert
        assertNotNull(controller.getProfile());

        // Clean up
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}
