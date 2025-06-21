package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderLineItem {
    private int orderLineItemId;
    private int orderId;
    private int productId;
    private Product product;
    private BigDecimal salesPrice;
    private int quantity;
    private BigDecimal discount = BigDecimal.ZERO;

    public BigDecimal getLineTotal() {
        BigDecimal sub = salesPrice.multiply(new BigDecimal(quantity));
        return sub.subtract(discount == null ? BigDecimal.ZERO : discount);
    }
}