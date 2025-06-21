package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}