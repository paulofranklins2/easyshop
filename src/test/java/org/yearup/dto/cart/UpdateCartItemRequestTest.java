package org.yearup.dto.cart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCartItemRequestTest {

    @Test
    void gettersAndSetters() {
        UpdateCartItemRequest req = new UpdateCartItemRequest();
        req.setQuantity(4);
        assertEquals(4, req.getQuantity());
    }
}
