package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.model.PromoCode;
import org.yearup.model.PromoCodeUsage;
import org.yearup.repository.PromoCodeRepository;
import org.yearup.repository.PromoCodeUsageRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service for creating and retrieving promo codes.
 */
@Service
public class PromoCodeService {
    private final PromoCodeRepository repo;
    private final PromoCodeUsageRepository usageRepo;

    public PromoCodeService(PromoCodeRepository repo, PromoCodeUsageRepository usageRepo) {
        this.repo = repo;
        this.usageRepo = usageRepo;
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

    public PromoCode update(int id, BigDecimal percent, String code) {
        PromoCode promo = repo.findById(id).orElse(null);
        if (promo != null) {
            if (percent != null) {
                promo.setDiscountPercent(percent);
            }
            if (code != null) {
                promo.setCode(code);
            }
            promo = repo.save(promo);
        }
        return promo;
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

    public PromoCode findByCode(String code) {
        return repo.findByCode(code);
    }

    public void recordUsage(int userId, PromoCode promo) {
        PromoCodeUsage usage = new PromoCodeUsage();
        usage.setUserId(userId);
        usage.setPromoCodeId(promo.getPromoCodeId());
        usage.setDiscountPercent(promo.getDiscountPercent());
        usage.setUsedDate(java.time.LocalDateTime.now());
        usageRepo.save(usage);
    }

    public List<PromoCodeUsage> getUsageForUser(int userId) {
        return usageRepo.findByUserIdOrderByUsedDateDesc(userId);
    }
}
