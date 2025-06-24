package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.model.Product;
import org.yearup.model.WishlistItemEntity;
import org.yearup.model.WishlistItemId;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.WishlistItemRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WishlistService {
    private final WishlistItemRepository repo;
    private final ProductRepository productRepo;

    public WishlistService(WishlistItemRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public List<Product> getWishlist(int userId) {
        return repo.findByUserId(userId)
            .stream()
            .map(e -> productRepo.findById(e.getProductId()).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public List<Product> addProduct(int userId, int productId) {
        if (!repo.existsByUserIdAndProductId(userId, productId)) {
            WishlistItemEntity entity = new WishlistItemEntity();
            entity.setUserId(userId);
            entity.setProductId(productId);
            repo.save(entity);
        }
        return getWishlist(userId);
    }

    public List<Product> deleteProduct(int userId, int productId) {
        repo.deleteById(new WishlistItemId(userId, productId));
        return getWishlist(userId);
    }
}
