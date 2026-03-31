package org.app.corporateinternetbanking.transaction;

import lombok.RequiredArgsConstructor;
import org.app.corporateinternetbanking.transaction.service.TransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredTransactionScheduler {

    private final TransactionService transactionService;

    @Scheduled(fixedDelay =5000 )
    public void  runExpirationJob(){
        transactionService.expirePendingTransactions();
    }
}
