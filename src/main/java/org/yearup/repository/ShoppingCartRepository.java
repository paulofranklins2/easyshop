package org.yearup.repository;

import org.yearup.model.ShoppingCart;

public interface ShoppingCartRepository {
    ShoppingCart getByUserId(int userId);

    ShoppingCart addProductToShoppingCart(int userId, int productId);

    ShoppingCart updateProductQuantity(int userId, int productId, int quantity);

    ShoppingCart deleteProductById(int userId, int productId);

    ShoppingCart clearCart(int userId);
}
