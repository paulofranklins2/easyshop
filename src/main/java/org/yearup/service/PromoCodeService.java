package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.model.PromoCode;
import org.yearup.repository.PromoCodeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service for creating and retrieving promo codes.
 */
@Service
public class PromoCodeService {
    private final PromoCodeRepository repo;

    public PromoCodeService(PromoCodeRepository repo) {
        this.repo = repo;
    }

    public List<PromoCode> getAll() {
        return repo.findAll();
    }

    public PromoCode create(BigDecimal percent) {
        PromoCode code = new PromoCode();
        code.setCode(UUID.randomUUID().toString().substring(0,8).toUpperCase());
        code.setDiscountPercent(percent);
        return repo.save(code);
    }

    public PromoCode findByCode(String code) {
        return repo.findByCode(code);
    }
}
