package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartItemEntityTest {

    @Test
    void gettersAndSetters() {
        ShoppingCartItemEntity entity = new ShoppingCartItemEntity();
        entity.setUserId(1);
        entity.setProductId(2);
        entity.setQuantity(3);

        assertEquals(1, entity.getUserId());
        assertEquals(2, entity.getProductId());
        assertEquals(3, entity.getQuantity());
    }
}
