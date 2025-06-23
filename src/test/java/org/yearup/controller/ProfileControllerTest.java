package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.yearup.model.Profile;
import org.yearup.model.User;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class ProfileControllerTest {

    @Test
    void getProfile() {
        ProfileRepository repo = org.mockito.Mockito.mock(ProfileRepository.class);
        UserRepository userRepo = org.mockito.Mockito.mock(UserRepository.class);
        ProfileController controller = new ProfileController(repo, userRepo);

        User user = new User(); user.setId(1); org.mockito.Mockito.when(userRepo.findByUsername("u")).thenReturn(user);
        org.mockito.Mockito.when(repo.findById(1)).thenReturn(java.util.Optional.of(new Profile()));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("u","p"));
        assertNotNull(controller.getProfile());
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}
