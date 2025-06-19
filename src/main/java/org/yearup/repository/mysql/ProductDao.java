package org.yearup.repository.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.model.Product;
import org.yearup.repository.ProductRepository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDao extends DataManager implements ProductRepository {

    @Autowired
    public ProductDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color, int offset, int limit) {
        List<Product> products = new ArrayList<>();

        String sql = """
                    SELECT * FROM products
                    WHERE (category_id = ? OR ? = -1)
                      AND (price >= ? OR ? = -1)
                      AND (price <= ? OR ? = -1)
                      AND (color = ? OR ? = '')
                    LIMIT ? OFFSET ?
                """;

        categoryId = (categoryId == null) ? -1 : categoryId;
        minPrice = (minPrice == null) ? new BigDecimal("-1") : minPrice;
        maxPrice = (maxPrice == null) ? new BigDecimal("-1") : maxPrice;
        color = (color == null) ? "" : color;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            statement.setInt(2, categoryId);
            statement.setBigDecimal(3, minPrice);
            statement.setBigDecimal(4, minPrice);
            statement.setBigDecimal(5, maxPrice);
            statement.setBigDecimal(6, maxPrice);
            statement.setString(7, color);
            statement.setString(8, color);

            statement.setInt(9, limit);
            statement.setInt(10, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching products: " + e);
        }

        return products;
    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listing products by category: " + e);
        }

        return products;
    }

    @Override
    public Product getById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving product by ID: " + e);
        }

        return null;
    }

    @Override
    public Product create(Product product) {
        String sql = """
                    INSERT INTO products(name, price, category_id, description, color, image_url, stock, featured)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setProductParams(statement, product);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        return getById(generatedId);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating product: " + e);
        }

        return null;
    }

    @Override
    public void update(int productId, Product product) {
        String sql = """
                    UPDATE products
                    SET name = ?, price = ?, category_id = ?, description = ?,
                        color = ?, image_url = ?, stock = ?, featured = ?
                    WHERE product_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setProductParams(statement, product);
            statement.setInt(9, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating product: " + e);
        }
    }

    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product: " + e);
        }
    }

    // Helper: Map a row to a Product object
    protected static Product mapRow(ResultSet row) throws SQLException {
        return new Product(
                row.getInt("product_id"),
                row.getString("name"),
                row.getBigDecimal("price"),
                row.getInt("category_id"),
                row.getString("description"),
                row.getString("color"),
                row.getInt("stock"),
                row.getBoolean("featured"),
                row.getString("image_url")
        );
    }

    // Helper: Set product parameters into a PreparedStatement
    private void setProductParams(PreparedStatement ps, Product product) throws SQLException {
        ps.setString(1, product.getName());
        ps.setBigDecimal(2, product.getPrice());
        ps.setInt(3, product.getCategoryId());
        ps.setString(4, product.getDescription());
        ps.setString(5, product.getColor());
        ps.setString(6, product.getImageUrl());
        ps.setInt(7, product.getStock());
        ps.setBoolean(8, product.isFeatured());
    }

    @Override
    public List<Product> searchByQuery(String query, int offset, int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ? LIMIT ? OFFSET ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String like = "%" + query + "%";
            statement.setString(1, like);
            statement.setString(2, like);
            statement.setInt(3, limit);
            statement.setInt(4, offset);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching products by query: " + e);
        }

        return products;
    }

    @Override
    public List<Product> listPaged(int offset, int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products LIMIT ? OFFSET ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error listing paged products: " + e);
        }

        return products;
    }
}
