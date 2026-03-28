package org.app.corporateinternetbanking.ledger.service;

import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.ledger.mode.LedgerEntry;
import org.app.corporateinternetbanking.transaction.model.Transaction;

import java.math.BigDecimal;

public interface LedgerService {
    LedgerEntry createEntry(Account account, Transaction transaction, EntryType type, String currency,BigDecimal amount,BigDecimal balanceAfter);
}
