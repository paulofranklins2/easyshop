package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.Profile;

/**
 * JPA repository for {@link Profile} entities.
 */
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}