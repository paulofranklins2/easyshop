package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yearup.model.PromoCode;
import org.yearup.model.PromoCodeUsage;
import org.yearup.repository.PromoCodeRepository;
import org.yearup.repository.PromoCodeUsageRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PromoCodeServiceTest {

    @Test
    void createGeneratesCodeAndSaves() {
        PromoCodeRepository repo = Mockito.mock(PromoCodeRepository.class);
        PromoCodeUsageRepository usageRepo = Mockito.mock(PromoCodeUsageRepository.class);
        Mockito.when(repo.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        PromoCodeService service = new PromoCodeService(repo, usageRepo);
        PromoCode result = service.create(new BigDecimal("0.25"));

        Mockito.verify(repo).save(Mockito.any());
        assertEquals(new BigDecimal("0.25"), result.getDiscountPercent());
        assertNotNull(result.getCode());
        assertEquals(8, result.getCode().length());
    }

    @Test
    void updateChangesExistingPromo() {
        PromoCodeRepository repo = Mockito.mock(PromoCodeRepository.class);
        PromoCodeUsageRepository usageRepo = Mockito.mock(PromoCodeUsageRepository.class);
        PromoCode existing = new PromoCode();
        existing.setPromoCodeId(1);
        existing.setCode("OLD");
        existing.setDiscountPercent(BigDecimal.ONE);

        Mockito.when(repo.findById(1)).thenReturn(Optional.of(existing));
        Mockito.when(repo.save(existing)).thenReturn(existing);

        PromoCodeService service = new PromoCodeService(repo, usageRepo);
        PromoCode updated = service.update(1, BigDecimal.TEN, "NEW");

        assertEquals(BigDecimal.TEN, updated.getDiscountPercent());
        assertEquals("NEW", updated.getCode());
        Mockito.verify(repo).save(existing);
    }

    @Test
    void updateReturnsNullWhenNotFound() {
        PromoCodeRepository repo = Mockito.mock(PromoCodeRepository.class);
        PromoCodeService service = new PromoCodeService(repo, Mockito.mock(PromoCodeUsageRepository.class));
        Mockito.when(repo.findById(5)).thenReturn(Optional.empty());

        assertNull(service.update(5, BigDecimal.ONE, "X"));
        Mockito.verify(repo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteDelegatesToRepository() {
        PromoCodeRepository repo = Mockito.mock(PromoCodeRepository.class);
        PromoCodeService service = new PromoCodeService(repo, Mockito.mock(PromoCodeUsageRepository.class));

        service.delete(2);
        Mockito.verify(repo).deleteById(2);
    }

    @Test
    void findAndListDelegatesToRepository() {
        PromoCodeRepository repo = Mockito.mock(PromoCodeRepository.class);
        PromoCode code = new PromoCode();
        List<PromoCode> list = List.of(code);
        Mockito.when(repo.findAll()).thenReturn(list);
        Mockito.when(repo.findByCode("ABC")).thenReturn(code);
        PromoCodeService service = new PromoCodeService(repo, Mockito.mock(PromoCodeUsageRepository.class));

        assertEquals(list, service.getAll());
        assertEquals(code, service.findByCode("ABC"));
    }

    @Test
    void recordUsageStoresUsageEntity() {
        PromoCodeRepository repo = Mockito.mock(PromoCodeRepository.class);
        PromoCodeUsageRepository usageRepo = Mockito.mock(PromoCodeUsageRepository.class);
        PromoCodeService service = new PromoCodeService(repo, usageRepo);
        PromoCode promo = new PromoCode();
        promo.setPromoCodeId(3);
        promo.setDiscountPercent(new BigDecimal("0.15"));

        service.recordUsage(7, promo);

        Mockito.verify(usageRepo).save(Mockito.argThat(u ->
            u.getUserId() == 7 &&
                u.getPromoCodeId() == 3 &&
                u.getDiscountPercent().equals(new BigDecimal("0.15")) &&
                u.getUsedDate() != null
        ));
    }

    @Test
    void getUsageForUserDelegates() {
        PromoCodeUsageRepository usageRepo = Mockito.mock(PromoCodeUsageRepository.class);
        List<PromoCodeUsage> list = List.of(new PromoCodeUsage());
        Mockito.when(usageRepo.findByUserIdOrderByUsedDateDesc(4)).thenReturn(list);
        PromoCodeService service = new PromoCodeService(Mockito.mock(PromoCodeRepository.class), usageRepo);

        assertEquals(list, service.getUsageForUser(4));
    }
}
