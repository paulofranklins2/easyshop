package org.yearup.model.dto.cart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateCartItemRequestTest {

    @Test
    void gettersAndSetters() {
        UpdateCartItemRequest req = new UpdateCartItemRequest();
        req.setQuantity(4);
        assertEquals(4, req.getQuantity());
    }
}
