package org.yearup.repository.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.yearup.model.User;
import org.yearup.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao extends DataManager implements UserRepository {
    @Autowired
    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public User create(User newUser) {
        String sql = "INSERT INTO users (username, hashed_password, role) VALUES (?, ?, ?)";
        String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, newUser.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, newUser.getRole());
            ps.executeUpdate();

            User user = getByUserName(newUser.getUsername());
            if (user != null) {
                user.setPassword(""); // clear raw password from response
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating user: " + e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all users: " + e);
        }

        return users;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet row = statement.executeQuery()) {
                if (row.next()) {
                    return mapRow(row);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user by ID: " + e);
        }

        return null;
    }

    @Override
    public User getByUserName(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet row = statement.executeQuery()) {
                if (row.next()) {
                    return mapRow(row);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user by username: " + e);
        }

        return null;
    }

    @Override
    public int getIdByUsername(String username) {
        User user = getByUserName(username);
        return (user != null) ? user.getId() : -1;
    }

    @Override
    public boolean exists(String username) {
        return getByUserName(username) != null;
    }

    // Helper method to map ResultSet to User
    private User mapRow(ResultSet row) throws SQLException {
        int userId = row.getInt("user_id");
        String username = row.getString("username");
        String hashedPassword = row.getString("hashed_password");
        String role = row.getString("role");

        return new User(userId, username, hashedPassword, role);
    }
}
