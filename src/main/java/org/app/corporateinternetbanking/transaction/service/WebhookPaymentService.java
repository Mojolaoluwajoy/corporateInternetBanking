package org.app.corporateinternetbanking.transaction.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.app.corporateinternetbanking.transaction.domain.entity.Transaction;
import org.app.corporateinternetbanking.transaction.domain.repository.TransactionRepository;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.exceptions.InvalidSignature;
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
    private WalletService walletService;

    public void handleWebhook(String payload,String signature) throws InvalidSignature {

        if (!isValid(payload,signature)){
            throw new InvalidSignature("Invalid signature");
        }

        JSONObject json = new JSONObject(payload);
          String event=json.getString("event");

          JSONObject data=json.getJSONObject("data");
          String reference=data.getString("reference");

        Transaction txn=transactionRepository.findByTransactionReference(reference).orElseThrow(()->new InvalidSignature("Invalid transaction reference"));

   if (txn.getStatus()== TransactionStatus.SUCCESS)return;

   if ("charge.success".equals(event)){
       walletService.credit(txn.getAmount());
       tr

       }

   }

    }

    private boolean isValid(String payload, String signature) {
        try {

        }catch (Exception e){

            return false;
        }
    }

}
