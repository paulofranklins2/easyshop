package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Line item within an order.
 */
@Getter
@Setter
@Entity
@Table(name = "order_line_items")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private int orderLineItemId;
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    private BigDecimal salesPrice;
    private int quantity;
    private BigDecimal discount = BigDecimal.ZERO;
    private LocalDateTime date;

    public BigDecimal getLineTotal() {
        BigDecimal sub = salesPrice.multiply(new BigDecimal(quantity));
        return sub.subtract(discount == null ? BigDecimal.ZERO : discount);
    }
}
