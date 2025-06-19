package org.yearup.repository;

import org.yearup.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User getUserById(int userId);

    User getByUserName(String username);

    int getIdByUsername(String username);

    User create(User user);

    boolean exists(String username);
}
