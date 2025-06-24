package org.yearup.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.yearup.exception.handler.UserNotActivatedException;
import org.yearup.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserDetailsService} that loads user data from the
 * database using {@link UserRepository}.
 */
@Component("userDetailsService")
public class DatabaseUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DatabaseUserDetailsService.class);
    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating user '{}'", login);
        String lowercaseLogin = login.toLowerCase();
        org.yearup.model.User user = userRepository.findByUsername(lowercaseLogin);
        if (user == null) {
            log.debug("User '{}' not found in the database", lowercaseLogin);
            throw new UsernameNotFoundException("User " + lowercaseLogin + " not found");
        }
        return createSpringSecurityUser(lowercaseLogin, user);
    }

    private User createSpringSecurityUser(String lowercaseLogin, org.yearup.model.User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());

        return new User(
            user.getUsername(),
            user.getPassword(),
            grantedAuthorities
        );
    }
}
