package org.yearup.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JwtConfigurerTest {

    @Test
    void configure_addsJwtFilterBeforeUsernamePasswordAuthenticationFilter() throws Exception {
        // Arrange
        TokenProvider provider = mock(TokenProvider.class);
        JwtConfigurer configurer = new JwtConfigurer(provider);

        HttpSecurity http = mock(HttpSecurity.class); // Needs mockito-inline to work
        when(http.addFilterBefore(any(JwtFilter.class), eq(UsernamePasswordAuthenticationFilter.class)))
            .thenReturn(http);

        // Act
        configurer.configure(http);

        // Assert
        verify(http).addFilterBefore(any(JwtFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
    }
}
