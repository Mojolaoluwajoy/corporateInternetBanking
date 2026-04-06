package org.app.corporateinternetbanking.currency.domain.repository;

import org.app.corporateinternetbanking.currency.domain.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {


    Optional<Currency> findByCode(String code);
}

