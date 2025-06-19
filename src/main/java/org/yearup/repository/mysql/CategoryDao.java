package org.yearup.repository.mysql;

import org.springframework.stereotype.Component;
import org.yearup.model.Category;
import org.yearup.repository.CategoryRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDao extends DataManager implements CategoryRepository {

    public CategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving categories: " + e);
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving category by ID: " + e);
        }

        return null;
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories(name, description) VALUES(?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            int rows = ps.executeUpdate();

            if (rows == 0) throw new SQLException("Creating category failed, no rows affected.");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Category(keys.getInt(1), category.getName(), category.getDescription());
                } else {
                    throw new SQLException("Creating category failed, no ID returned.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating category: " + e);
        }
    }

    @Override
    public boolean update(int categoryId, Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, categoryId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating category: " + e);
        }
    }

    @Override
    public boolean delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category: " + e);
        }
    }

    // Helper to map a ResultSet row to a Category
    private Category mapRow(ResultSet row) throws SQLException {
        int id = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Category(id, name, description);
    }
}
