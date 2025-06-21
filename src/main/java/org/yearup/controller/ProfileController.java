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


@RestController
public class ProfileController {
    private final ProfileRepository profileDao;

    public ProfileController(ProfileRepository profileDao) {
        this.profileDao = profileDao;
    }

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

        int userId = profileDao.findIdByUsername(username);
        return profileDao.findById(userId);
    }

    @PutMapping("/profile")
    public boolean updateProfile(@RequestBody Profile profile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        int userId = profileDao.findIdByUsername(username);
        profile.setUserId(userId);

        return profileDao.update(profile);
    }
}
