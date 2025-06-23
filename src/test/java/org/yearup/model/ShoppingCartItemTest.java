package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartItemTest {

    @Test
    void calculations() {
        Product p = new Product();
        p.setProductId(9);
        p.setPrice(java.math.BigDecimal.TEN);
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(p);
        item.setQuantity(2);
        item.setDiscountPercent(new java.math.BigDecimal("0.1"));

        assertEquals(9, item.getProductId());
        assertEquals(new java.math.BigDecimal("18.0"), item.getLineTotal());
    }
}
