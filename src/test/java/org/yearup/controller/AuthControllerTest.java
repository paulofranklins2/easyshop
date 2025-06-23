package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        ProfileRepository profileRepo = Mockito.mock(ProfileRepository.class);
        TokenProvider provider = Mockito.mock(TokenProvider.class);
        AuthenticationManagerBuilder builder = Mockito.mock(AuthenticationManagerBuilder.class);
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
        AuthController controller = new AuthController(provider, builder, userRepo, profileRepo, encoder);
        RegisterRequest req = new RegisterRequest();
        req.setUsername("u");
        req.setPassword("p");
        req.setConfirmPassword("p");
        req.setRole("USER");
        Mockito.when(userRepo.existsByUsername("u")).thenReturn(false);
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(new User("u", "p", "USER"));
        var response = controller.register(req);
        assertEquals(201, response.getStatusCodeValue());
    }
}
