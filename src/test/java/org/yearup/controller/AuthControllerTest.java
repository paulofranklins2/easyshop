package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.yearup.dto.auth.RegisterRequest;
import org.yearup.model.User;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.security.jwt.TokenProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthControllerTest {

    @Test
    void login() {
    }

    @Test
    void register() {
        UserRepository userRepo = org.mockito.Mockito.mock(UserRepository.class);
        ProfileRepository profileRepo = org.mockito.Mockito.mock(ProfileRepository.class);
        TokenProvider provider = org.mockito.Mockito.mock(TokenProvider.class);
        AuthenticationManagerBuilder builder = org.mockito.Mockito.mock(AuthenticationManagerBuilder.class);
        org.springframework.security.crypto.password.PasswordEncoder encoder = org.mockito.Mockito.mock(org.springframework.security.crypto.password.PasswordEncoder.class);
        AuthController controller = new AuthController(provider, builder, userRepo, profileRepo, encoder);
        RegisterRequest req = new RegisterRequest();
        req.setUsername("u");
        req.setPassword("p");
        req.setConfirmPassword("p");
        req.setRole("USER");
        org.mockito.Mockito.when(userRepo.existsByUsername("u")).thenReturn(false);
        org.mockito.Mockito.when(userRepo.save(org.mockito.Mockito.any())).thenReturn(new User("u", "p", "USER"));
        var response = controller.register(req);
        assertEquals(201, response.getStatusCodeValue());
    }
}
