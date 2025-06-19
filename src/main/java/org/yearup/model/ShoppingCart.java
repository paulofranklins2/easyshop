package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ShoppingCart {
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

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
        BigDecimal total = items.values()
                .stream()
                .map(i -> i.getLineTotal())
                .reduce(BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));

        return total;
    }

}
