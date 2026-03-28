package org.app.corporateinternetbanking.ledger.service;

import lombok.AllArgsConstructor;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.ledger.mode.LedgerEntry;
import org.app.corporateinternetbanking.ledger.repository.LedgerRepository;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class LedgerServiceImpl implements LedgerService {
    private final LedgerRepository ledgerRepository;

    @Override
    public LedgerEntry createEntry(Account account, Transaction transaction, EntryType type,String currency, BigDecimal amount, BigDecimal balanceAfter) {
        LedgerEntry entry=new LedgerEntry();
        entry.setAccount(account);
        entry.setTransaction(transaction);
        entry.setType(type);
        entry.setCurrency(currency);
        entry.setAmount(amount);
        entry.setBalanceAfter(balanceAfter);
        return ledgerRepository.save(entry);
    }
//    @Override
//    public LedgerCreationResponse createEntry(LedgerCreationRequest request) {
//        LedgerEntry entry=LedgerMap .mapCreationRequest(request);
//        LedgerEntry savedResponse= ledgerRepository.save(entry);
//      return   LedgerMap.mapCreationResponse(savedResponse);
//    }
}
