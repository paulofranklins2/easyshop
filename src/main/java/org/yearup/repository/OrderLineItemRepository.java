package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.OrderLineItem;

import java.util.List;

/**
 * Repository for {@link OrderLineItem} entities.
 */
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Integer> {
    List<OrderLineItem> findByOrderId(int orderId);
}