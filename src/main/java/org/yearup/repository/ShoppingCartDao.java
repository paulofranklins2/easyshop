package org.yearup.repository;

import org.yearup.model.ShoppingCart;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
}
