package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.User;

/**
 * Repository for user accounts.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}