package org.app.corporateinternetbanking.currency.repository;

import org.app.corporateinternetbanking.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {


    Optional<Currency> findByCode(String code);
}

