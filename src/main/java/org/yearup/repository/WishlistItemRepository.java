package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.WishlistItemEntity;
import org.yearup.model.WishlistItemId;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, WishlistItemId> {
    List<WishlistItemEntity> findByUserId(int userId);
    boolean existsByUserIdAndProductId(int userId, int productId);
}
