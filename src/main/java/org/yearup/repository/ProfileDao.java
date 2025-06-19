package org.yearup.repository;


import org.yearup.model.Profile;

public interface ProfileDao {
    Profile create(Profile profile);
    boolean update(Profile profile);
    Profile findById(int id);
    int findIdByUsername(String username);
}
