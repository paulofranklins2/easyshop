package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.model.Profile;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.exception.UnauthorizedException;


/**
 * Controller for retrieving and updating user profiles.
 */
@RestController
public class ProfileController {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);
    private final ProfileRepository profileDao;
    private final UserRepository userRepository;

    public ProfileController(ProfileRepository profileDao, UserRepository userRepository) {
        this.profileDao = profileDao;
        this.userRepository = userRepository;
    }

    /**
     * Get the profile for the currently authenticated user.
     */
    @GetMapping("/profile")
    public Profile getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LOG.debug("Fetching profile for current user");

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Unauthorized access.");
        }

        String username;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        int userId = userRepository.findByUsername(username).getId();
        return profileDao.findById(userId).orElse(null);
    }

    /**
     * Update the profile for the current user.
     */
    @PutMapping("/profile")
    public boolean updateProfile(@RequestBody Profile profile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        LOG.debug("Updating profile for user '{}'", username);

        int userId = userRepository.findByUsername(username).getId();
        profile.setUserId(userId);
        profileDao.save(profile);
        return true;
    }
}
