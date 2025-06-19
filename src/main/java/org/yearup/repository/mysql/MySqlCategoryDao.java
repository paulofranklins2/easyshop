package org.yearup.repository.mysql;

import org.springframework.stereotype.Component;
import org.yearup.repository.CategoryDao;
import org.yearup.model.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        String sql = "select * from categories";
        ArrayList<Category> categories = new ArrayList<>();

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong" + e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = "select * from categories where category_id = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"));

            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong: " + e);
        }
    }

    @Override
    public Category create(Category category) {
        String sql = "insert into categories(name, description) values(?, ?)";
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) throw new SQLException("Creating category failed, no rows affected.");

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new Category(generatedId, category.getName(), category.getDescription());
                } else throw new SQLException("Creating category failed, no ID obtained.");

            }

        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong: " + e);
        }
    }

    @Override
    public boolean update(int categoryId, Category category) {
        if (getById(categoryId) != null) {
            String sql = "update categories set name=?, description=? where category_id = ?";
            try (Connection connection = this.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setInt(3, categoryId);
                return preparedStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong: " + e);
            }
        }
        return false;
    }

    @Override
    public boolean delete(int categoryId) {
        if (getById(categoryId) != null) {
            String sql = "delete from categories where category_id = ?";
            try (Connection connection = this.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, categoryId);
                return preparedStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong: " + e);
            }
        }
        return false;
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
