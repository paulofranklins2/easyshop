package org.yearup.controller;

import org.springframework.web.bind.annotation.*;
import org.yearup.model.Product;
import org.yearup.model.ShoppingCart;
import org.yearup.model.User;
import org.yearup.repository.UserRepository;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.WishlistService;
import org.yearup.exception.NotFoundException;
import org.yearup.exception.UnauthorizedException;

import java.security.Principal;
import java.util.List;

@RestController
public class WishlistController {
    private final WishlistService wishlistService;
    private final ShoppingCartService cartService;
    private final UserRepository userRepo;

    public WishlistController(WishlistService wishlistService, ShoppingCartService cartService, UserRepository userRepo) {
        this.wishlistService = wishlistService;
        this.cartService = cartService;
        this.userRepo = userRepo;
    }

    @GetMapping("/wishlist")
    public List<Product> getWishlist(Principal principal) {
        int userId = getUserId(principal);
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/wishlist/{productId}")
    public List<Product> add(@PathVariable int productId, Principal principal) {
        int userId = getUserId(principal);
        return wishlistService.addProduct(userId, productId);
    }

    @DeleteMapping("/wishlist/{productId}")
    public List<Product> delete(@PathVariable int productId, Principal principal) {
        int userId = getUserId(principal);
        return wishlistService.deleteProduct(userId, productId);
    }

    @PostMapping("/wishlist/{productId}/cart")
    public ShoppingCart moveToCart(@PathVariable int productId, Principal principal) {
        int userId = getUserId(principal);
        wishlistService.deleteProduct(userId, productId);
        return cartService.addProduct(userId, productId);
    }

    private int getUserId(Principal principal) {
        if (principal == null) throw new UnauthorizedException("Authentication required");
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        if (user == null) throw new NotFoundException("User not found");
        return user.getId();
    }
}
