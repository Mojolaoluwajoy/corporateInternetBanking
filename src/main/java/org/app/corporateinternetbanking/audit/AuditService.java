package org.app.corporateinternetbanking.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.account.repository.AccountRepository;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.ledger.mode.LedgerEntry;
import org.app.corporateinternetbanking.ledger.repository.LedgerRepository;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;

    public void auditBalances(){

        List<Account> accounts=accountRepository.findAll();

        for (Account account:accounts){

//List <LedgerEntry> entries=ledgerRepository.findByAccountId(account.getId());

           LedgerEntry lastLedger =ledgerRepository.findByAccountIdOrderByCreatedAtDesc(account.getId());
          if (lastLedger==null) continue;

            BigDecimal storedBalance=account.getBalance();
            BigDecimal computedBalance=lastLedger.getBalanceAfter();



            if (storedBalance.compareTo(computedBalance)!=0) {
                account.setFlagged(true);
                log.info("MISMATCH DETECTED FOR ACCOUNT: {},STORED BALANCE: {},COMPUTED BALANCE: {}",account.getId(),storedBalance,computedBalance);
            }
            else {
                account.setFlagged(false);
            }
        }

    }
}
