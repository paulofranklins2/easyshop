package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingCartTest {

    @Test
    void contains() {
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartItem item = new ShoppingCartItem();
        Product p = new Product();
        p.setProductId(1);
        p.setPrice(java.math.BigDecimal.TEN);
        item.setProduct(p);
        cart.add(item);

        assertTrue(cart.contains(1));
        assertEquals(item, cart.get(1));
        assertEquals(java.math.BigDecimal.TEN, cart.getTotal());
    }
}
