package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.dto.auth.LoginRequest;
import org.yearup.dto.auth.LoginResponse;
import org.yearup.dto.auth.RegisterRequest;
import org.yearup.model.Profile;
import org.yearup.model.User;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.security.jwt.JwtFilter;
import org.yearup.security.jwt.TokenProvider;

import javax.validation.Valid;

/**
 * Controller responsible for authentication endpoints such as login and registration.
 */
@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userDao;
    private final ProfileRepository profileDao;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TokenProvider tokenProvider,
                          AuthenticationManagerBuilder authenticationManagerBuilder,
                          UserRepository userDao, ProfileRepository profileDao,
                          PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticate a user and return a JWT token.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LOG.debug("Login attempt for user '{}'", loginRequest.getUsername());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, false);

            User user = userDao.findByUsername(loginRequest.getUsername());
            if (user == null) {
                throw new UsernameNotFoundException("user not found");
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new LoginResponse(jwt, user), httpHeaders, HttpStatus.OK);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            LOG.warn("Invalid login for user '{}'", loginRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        } catch (Exception ex) {
            LOG.error("Error logging in user {}", loginRequest.getUsername(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    /**
     * Register a new user account.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest newUser) {
        LOG.debug("Registering user '{}'", newUser.getUsername());
        try {
            boolean exists = userDao.existsByUsername(newUser.getUsername());
            if (exists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already Exists.");
            }

            String role = newUser.getRole();
            if (role == null || role.isBlank()) {
                role = "USER";
            }

            // create user
            String hashed = passwordEncoder.encode(newUser.getPassword());
            User user = userDao.save(new User(newUser.getUsername(), hashed, role));
            // create profile
            Profile profile = new Profile(user.getId(), "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
            profileDao.save(profile);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error("Error registering user {}", newUser.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}
