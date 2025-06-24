package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.yearup.model.User;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.service.InvoiceService;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @Test
    void listOrders() {
        OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
        OrderLineItemRepository lineRepo = Mockito.mock(OrderLineItemRepository.class);
        ShoppingCartService cartService = Mockito.mock(ShoppingCartService.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        ProfileRepository profileRepo = Mockito.mock(ProfileRepository.class);
        InvoiceService invoiceService = Mockito.mock(InvoiceService.class);
        OrderController controller = new OrderController(orderRepo, lineRepo, cartService, userRepo, profileRepo, invoiceService);

        User u = new User();
        u.setId(1);
        Mockito.when(userRepo.findByUsername("u")).thenReturn(u);
        Mockito.when(orderRepo.findByUserIdOrderByDateDesc(1)).thenReturn(List.of());

        Principal principal = () -> "u";
        assertTrue(controller.listOrders(principal).isEmpty());
    }

    @Test
    void createOrderHasCreatedStatus() throws NoSuchMethodException {
        var method = OrderController.class.getMethod("createOrder", Principal.class);
        var annotation = method.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.CREATED, annotation.value());
    }

    @Test
    void downloadInvoiceMethodExists() throws NoSuchMethodException {
        var method = OrderController.class.getMethod("downloadInvoice", int.class, Principal.class);
        assertNotNull(method);
    }
}
