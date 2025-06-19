package org.yearup.repository.mysql;

import org.springframework.stereotype.Component;
import org.yearup.model.Profile;
import org.yearup.repository.ProfileRepository;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class ProfileDao extends DataManager implements ProfileRepository {

    public ProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile) {
        String sql = """
                    INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setProfileParameters(ps, profile, false);
            ps.executeUpdate();
            return profile;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating profile: " + e);
        }
    }

    @Override
    public boolean update(Profile profile) {
        String sql = """
                    UPDATE profiles
                    SET first_name = ?, last_name = ?, phone = ?, email = ?, address = ?, city = ?, state = ?, zip = ?
                    WHERE user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            setProfileParameters(ps, profile, true); // include user_id at end
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile: " + e);
        }
    }

    @Override
    public Profile findById(int id) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProfile(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding profile: " + e);
        }

        return null;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("User lookup failed", e);
        }

        throw new RuntimeException("User not found");
    }

    // Helper: maps a ResultSet row to a Profile object
    private Profile mapRowToProfile(ResultSet rs) throws SQLException {
        return new Profile(
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip")
        );
    }

    // Helper: sets profile parameters into a PreparedStatement
    private void setProfileParameters(PreparedStatement ps, Profile profile, boolean includeUserIdAtEnd) throws SQLException {
        ps.setString(1, profile.getFirstName());
        ps.setString(2, profile.getLastName());
        ps.setString(3, profile.getPhone());
        ps.setString(4, profile.getEmail());
        ps.setString(5, profile.getAddress());
        ps.setString(6, profile.getCity());
        ps.setString(7, profile.getState());
        ps.setString(8, profile.getZip());

        if (includeUserIdAtEnd)
            ps.setInt(9, profile.getUserId());
        else
            ps.setInt(1, profile.getUserId()); // only for INSERT if needed
    }
}
