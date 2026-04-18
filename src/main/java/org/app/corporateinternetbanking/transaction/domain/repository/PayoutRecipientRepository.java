package org.app.corporateinternetbanking.transaction.domain.repository;

import org.app.corporateinternetbanking.transaction.domain.entity.PayoutRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayoutRecipientRepository extends JpaRepository<PayoutRecipient, Long> {

    Optional<PayoutRecipient> findByAccountNumberAndBankCode(String accountNumber, String bankCode);


}
