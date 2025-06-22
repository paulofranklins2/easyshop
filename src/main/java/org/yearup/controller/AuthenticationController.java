package org.yearup.controller;

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
import org.yearup.model.Profile;
import org.yearup.model.User;
import org.yearup.model.authentication.LoginDto;
import org.yearup.model.authentication.LoginResponseDto;
import org.yearup.model.authentication.RegisterUserDto;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.security.jwt.JWTFilter;
import org.yearup.security.jwt.TokenProvider;

import javax.validation.Valid;

/**
 * Controller responsible for authentication endpoints such as login and registration.
 */
@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userDao;
    private final ProfileRepository profileDao;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(TokenProvider tokenProvider,
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
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, false);

            User user = userDao.findByUsername(loginDto.getUsername());
            if (user == null) {
                throw new UsernameNotFoundException("user not found");
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    /**
     * Register a new user account.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto newUser) {

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}
