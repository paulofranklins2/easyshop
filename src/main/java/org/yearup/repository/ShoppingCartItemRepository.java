package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.ShoppingCartItemEntity;
import org.yearup.model.ShoppingCartItemId;

import java.util.List;

/**
 * Repository for persisting shopping cart items.
 */
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItemEntity, ShoppingCartItemId> {
    List<ShoppingCartItemEntity> findByUserId(int userId);
}