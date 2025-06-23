package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.PromoCode;

/**
 * Repository for {@link PromoCode} records.
 */
public interface PromoCodeRepository extends JpaRepository<PromoCode, Integer> {
    PromoCode findByCode(String code);
}
