package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.yearup.model.User;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.service.ShoppingCartService;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @Test
    void listOrders() {
        OrderRepository orderRepo = org.mockito.Mockito.mock(OrderRepository.class);
        OrderLineItemRepository lineRepo = org.mockito.Mockito.mock(OrderLineItemRepository.class);
        ShoppingCartService cartService = org.mockito.Mockito.mock(ShoppingCartService.class);
        UserRepository userRepo = org.mockito.Mockito.mock(UserRepository.class);
        ProfileRepository profileRepo = org.mockito.Mockito.mock(ProfileRepository.class);
        OrderController controller = new OrderController(orderRepo, lineRepo, cartService, userRepo, profileRepo);

        User u = new User(); u.setId(1); org.mockito.Mockito.when(userRepo.findByUsername("u")).thenReturn(u);
        org.mockito.Mockito.when(orderRepo.findByUserIdOrderByDateDesc(1)).thenReturn(java.util.List.of());

        java.security.Principal principal = () -> "u";
        assertTrue(controller.listOrders(principal).isEmpty());
    }

    @Test
    void createOrderHasCreatedStatus() throws NoSuchMethodException {
        var method = OrderController.class.getMethod("createOrder", java.security.Principal.class);
        var annotation = method.getAnnotation(org.springframework.web.bind.annotation.ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(org.springframework.http.HttpStatus.CREATED, annotation.value());
    }
}
