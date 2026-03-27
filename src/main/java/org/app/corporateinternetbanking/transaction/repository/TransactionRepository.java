package org.app.corporateinternetbanking.transaction.repository;

import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {



}
