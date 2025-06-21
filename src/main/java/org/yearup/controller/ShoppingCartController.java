package org.yearup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.model.ShoppingCart;
import org.yearup.model.User;
import org.yearup.model.dto.UpdateCartItemDto;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.ShoppingCartRepository;
import org.yearup.repository.UserRepository;

import java.security.Principal;

@RestController
public class ShoppingCartController {
    // a shopping cart requires
    private final ShoppingCartRepository shoppingCartDao;
    private final UserRepository userDao;

    @Autowired
    public ShoppingCartController(ShoppingCartRepository shoppingCartDao, UserRepository userDao, ProductRepository productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    @GetMapping("/cart")
    public ShoppingCart getCart(Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            return shoppingCartDao.getByUserId(userId);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error.");
        }
    }

    @PostMapping("/cart/products/{productId}")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.addProductToShoppingCart(userId, productId);
    }

    @PutMapping("/cart/products/{productId}")
    public ShoppingCart updateCartQuantity(@PathVariable int productId,
                                           @RequestBody UpdateCartItemDto item,
                                           Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.updateProductQuantity(userId, productId, item.getQuantity());
    }

    @DeleteMapping("/cart/products/{productId}")
    public ShoppingCart deleteProduct(@PathVariable int productId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.deleteProductById(userId, productId);
    }

    @DeleteMapping("/cart")
    public ShoppingCart clearCart(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.clearCart(userId);
    }

    private int getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user.getId();
    }
}
