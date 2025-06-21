package org.yearup.repository;

import org.yearup.model.Order;
import org.yearup.model.OrderLineItem;

import java.util.List;

public interface OrderRepository {
    Order create(Order order);
    void createLineItem(OrderLineItem item);
    List<Order> listByUserId(int userId);
    Order getById(int orderId);
    List<OrderLineItem> getLineItemsByOrderId(int orderId);
}