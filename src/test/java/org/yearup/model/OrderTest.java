package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    void totalCalculation() {
        Order order = new Order();
        OrderLineItem line = new OrderLineItem();
        line.setSalesPrice(java.math.BigDecimal.TEN);
        line.setQuantity(2);
        order.setItems(java.util.List.of(line));
        order.setShippingAmount(java.math.BigDecimal.ONE);

        assertEquals(new java.math.BigDecimal("21"), order.getTotal());
    }
}
