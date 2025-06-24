package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yearup.model.PromoCode;
import org.yearup.model.PromoCodeUsage;
import org.yearup.model.User;
import org.yearup.repository.UserRepository;
import org.yearup.service.PromoCodeService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromoCodeControllerTest {

    @Test
    void listDelegatesToService() {
        PromoCodeService service = Mockito.mock(PromoCodeService.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        PromoCodeController controller = new PromoCodeController(service, repo);
        List<PromoCode> codes = List.of(new PromoCode());
        Mockito.when(service.getAll()).thenReturn(codes);
        assertEquals(codes, controller.list());
    }

    @Test
    void createDelegatesToService() {
        PromoCodeService service = Mockito.mock(PromoCodeService.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        PromoCodeController controller = new PromoCodeController(service, repo);
        PromoCode code = new PromoCode();
        Mockito.when(service.create(BigDecimal.ONE)).thenReturn(code);
        assertEquals(code, controller.create(BigDecimal.ONE));
    }

    @Test
    void updateDelegatesToService() {
        PromoCodeService service = Mockito.mock(PromoCodeService.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        PromoCodeController controller = new PromoCodeController(service, repo);
        PromoCode code = new PromoCode();
        Mockito.when(service.update(1, BigDecimal.TEN, "X")).thenReturn(code);
        assertEquals(code, controller.update(1, BigDecimal.TEN, "X"));
    }

    @Test
    void deleteDelegatesToService() {
        PromoCodeService service = Mockito.mock(PromoCodeService.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        PromoCodeController controller = new PromoCodeController(service, repo);
        controller.delete(2);
        Mockito.verify(service).delete(2);
    }

    @Test
    void historyResolvesPrincipalUsername() {
        PromoCodeService service = Mockito.mock(PromoCodeService.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        PromoCodeController controller = new PromoCodeController(service, repo);
        User user = new User();
        user.setId(5);
        Mockito.when(repo.findByUsername("bob")).thenReturn(user);
        Principal principal = () -> "bob";
        List<PromoCodeUsage> list = List.of(new PromoCodeUsage());
        Mockito.when(service.getUsageForUser(5)).thenReturn(list);
        assertEquals(list, controller.history(principal));
    }

    @Test
    void historyUsesDefaultIdWhenPrincipalNull() {
        PromoCodeService service = Mockito.mock(PromoCodeService.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        PromoCodeController controller = new PromoCodeController(service, repo);
        List<PromoCodeUsage> list = List.of();
        Mockito.when(service.getUsageForUser(-1)).thenReturn(list);
        assertEquals(list, controller.history(null));
    }
}
