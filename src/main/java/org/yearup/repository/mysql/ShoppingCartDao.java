package org.yearup.repository.mysql;

import org.springframework.stereotype.Component;
import org.yearup.model.Product;
import org.yearup.model.ShoppingCart;
import org.yearup.model.ShoppingCartItem;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.ShoppingCartRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShoppingCartDao extends DataManager implements ShoppingCartRepository {
    private final ProductRepository productDao;

    public ShoppingCartDao(DataSource dataSource, ProductRepository productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapResultSetToCart(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching shopping cart for user_id = " + userId, e);
        }
    }

    @Override
    public ShoppingCart addProductToShoppingCart(int userId, int productId) {
        String sql = """
                    INSERT INTO shopping_cart (user_id, product_id, quantity)
                    VALUES (?, ?, 1)
                    ON DUPLICATE KEY UPDATE quantity = quantity + 1
                """;

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add product to cart", e);
        }

        return getByUserId(userId);
    }

    @Override
    public ShoppingCart updateProductQuantity(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to update product quantity", e);
        }

        return getByUserId(userId);
    }

    @Override
    public ShoppingCart deleteProductById(int userId, int productId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete product from cart", e);
        }

        return getByUserId(userId);
    }

    @Override
    public ShoppingCart clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to clear shopping cart", e);
        }

        return getByUserId(userId);
    }

    private ShoppingCart mapResultSetToCart(ResultSet resultSet) throws SQLException {
        ShoppingCart cart = new ShoppingCart();

        while (resultSet.next()) {
            int productId = resultSet.getInt("product_id");
            int quantity = resultSet.getInt("quantity");

            Product product = productDao.findById(productId).orElse(null);
            if (product == null) continue;

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(quantity);

            cart.add(item);
        }

        return cart;
    }
}
