package org.app.corporateinternetbanking.transaction.repository;

import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


     List<Transaction> findByStatus(TransactionStatus status);
}
