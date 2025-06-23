package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingCartItemIdTest {

    @Test
    void basicMethods() {
        ShoppingCartItemId id1 = new ShoppingCartItemId(1, 2);
        ShoppingCartItemId id2 = new ShoppingCartItemId(1, 2);

        assertEquals(1, id1.getUserId());
        assertEquals(2, id1.getProductId());
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertTrue(id1.toString().contains("userId=1"));
    }
}
