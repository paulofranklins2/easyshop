package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a completed order made by a user.
 */
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;
    private int userId;
    private LocalDateTime date;
    private String address;
    private String city;
    private String state;
    private String zip;
    private BigDecimal shippingAmount;
    @Column(name = "promo_code")
    private String promoCode;
    @Column(name = "discount_percent")
    private BigDecimal discountPercent = BigDecimal.ZERO;
    @Transient
    private BigDecimal total;
    @Transient
    private BigDecimal discountTotal;

    @Transient
    private List<OrderLineItem> items = new ArrayList<>();

    public void setItems(List<OrderLineItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        this.total = null;
        this.discountTotal = null;
    }

    public BigDecimal getTotal() {
        if (total == null) {
            BigDecimal sum = items.stream()
                .map(OrderLineItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (shippingAmount != null) {
                sum = sum.add(shippingAmount);
            }
            total = sum;
        }
        return total;
    }

    public BigDecimal getDiscountTotal() {
        if (discountTotal == null) {
            discountTotal = items.stream()
                .map(i -> i.getDiscount() == null ? BigDecimal.ZERO : i.getDiscount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return discountTotal;
    }
}
