package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Transient
    private BigDecimal total;

    @Transient
    private List<OrderLineItem> items = new ArrayList<>();

    public void setItems(List<OrderLineItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        this.total = null;
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
}