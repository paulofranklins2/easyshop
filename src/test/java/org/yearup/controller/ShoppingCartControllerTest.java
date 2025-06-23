package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.yearup.model.ShoppingCart;
import org.yearup.model.User;
import org.yearup.repository.UserRepository;
import org.yearup.service.ShoppingCartService;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartControllerTest {

    @Test
    void getCart() {
        ShoppingCartService service = org.mockito.Mockito.mock(ShoppingCartService.class);
        UserRepository repo = org.mockito.Mockito.mock(UserRepository.class);
        ShoppingCartController controller = new ShoppingCartController(service, repo);
        User u = new User();
        u.setId(1);
        org.mockito.Mockito.when(repo.findByUsername("user")).thenReturn(u);
        org.mockito.Mockito.when(service.getCart(1)).thenReturn(new ShoppingCart());
        java.security.Principal principal = () -> "user";
        assertNotNull(controller.getCart(principal));
    }
}
