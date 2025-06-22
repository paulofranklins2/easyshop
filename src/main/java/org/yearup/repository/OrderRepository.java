package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.Order;

import java.util.List;

/**
 * Repository for accessing {@link Order} data.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserIdOrderByDateDesc(int userId);
}