package org.app.corporateinternetbanking.transaction.service;

import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.service.AccountService;
import org.app.corporateinternetbanking.transaction.domain.entity.Transaction;
import org.app.corporateinternetbanking.transaction.domain.repository.TransactionRepository;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.exceptions.InvalidSignature;
import org.app.corporateinternetbanking.transaction.exceptions.TransactionDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebhookPaymentService {

    @Value("${paystack.secret.key}")
    private String secretKey;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    public void handleWebhook(String payload) throws InvalidSignature, AccountDoesNotExist, TransactionDoesNotExist {

        JSON json = new JSONObject(payload);
          String event=json.getString("event");

          JSONObject data=json.getJSONObject("data");
          String reference=data.getString("reference");

        Transaction txn=transactionRepository.findByIdempotencyKey(reference).orElseThrow(()->new InvalidSignature("Invalid transaction reference"));

   if (txn.getStatus()== TransactionStatus.SUCCESS)return;

   if ("charge.success".equals(event)){
       accountService.credit(txn.getDestinationAccount().getId(),txn.getAmount());
       transactionService.markSuccess(reference);
       }
   if ("charge.failed".equals(event)){
       accountService.credit(txn.getSourceAccount().getId(),txn.getAmount());
       transactionService.markFailed(reference);
   }

   }

}
