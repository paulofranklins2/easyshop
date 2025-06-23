package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineItemTest {

    @Test
    void lineTotalCalculation() {
        OrderLineItem item = new OrderLineItem();
        item.setSalesPrice(java.math.BigDecimal.TEN);
        item.setQuantity(2);
        item.setDiscount(java.math.BigDecimal.ONE);
        assertEquals(new java.math.BigDecimal("19"), item.getLineTotal());
    }
}
