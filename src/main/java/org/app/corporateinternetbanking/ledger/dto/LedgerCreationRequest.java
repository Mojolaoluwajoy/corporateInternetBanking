package org.app.corporateinternetbanking.ledger.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.transaction.model.Transaction;

import java.math.BigDecimal;
@Setter
@Getter
public class LedgerCreationRequest {
   private Account account;
   private Transaction transaction;
  private EntryType entryType;
  private BigDecimal amount;
   private BigDecimal balanceAfter;
}
