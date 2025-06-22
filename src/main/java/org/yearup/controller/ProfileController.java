package org.yearup.controller;

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


/**
 * Controller for retrieving and updating user profiles.
 */
@RestController
public class ProfileController {
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

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized access.");
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

        int userId = userRepository.findByUsername(username).getId();
        profile.setUserId(userId);
        profileDao.save(profile);
        return true;
    }
}