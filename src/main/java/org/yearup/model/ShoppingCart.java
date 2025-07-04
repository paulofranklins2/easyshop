package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory representation of a user's shopping cart.
 */
@Setter
@Getter
public class ShoppingCart {
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();
    private BigDecimal discountPercent = BigDecimal.ZERO;
    private String promoCode;

    public boolean contains(int productId) {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item) {
        items.put(item.getProductId(), item);
    }

    public ShoppingCartItem get(int productId) {
        return items.get(productId);
    }

    public BigDecimal getTotal() {
        return items.values()
                .stream()
                .map(ShoppingCartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));
    }
}
