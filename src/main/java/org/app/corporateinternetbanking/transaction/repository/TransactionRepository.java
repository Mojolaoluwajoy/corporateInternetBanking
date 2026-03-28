package org.app.corporateinternetbanking.transaction.repository;

import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


     List<Transaction> findByStatus(TransactionStatus status);

  Optional<Transaction> findByTransactionReference(String transactionReference);

}
