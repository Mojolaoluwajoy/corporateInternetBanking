package org.app.corporateinternetbanking.transaction.service;

import lombok.RequiredArgsConstructor;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.InvalidAccount;
import org.app.corporateinternetbanking.integration.paystack.PayStackClient;
import org.app.corporateinternetbanking.transaction.domain.entity.PayoutRecipient;
import org.app.corporateinternetbanking.transaction.domain.repository.PayoutRecipientRepository;
import org.app.corporateinternetbanking.transaction.dto.PayoutRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.dto.TransferRequest;
import org.app.corporateinternetbanking.transaction.exceptions.DuplicateTransaction;
import org.app.corporateinternetbanking.transaction.exceptions.InsufficientBalance;
import org.app.corporateinternetbanking.transaction.exceptions.InvalidAmount;
import org.app.corporateinternetbanking.transaction.exceptions.IsNull;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.app.corporateinternetbanking.user.exceptions.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.app.corporateinternetbanking.transaction.utils.mapper.PayoutMap.getPayoutRecipient;
import static org.app.corporateinternetbanking.transaction.utils.mapper.PayoutMap.mapToPayoutRequest;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final PayoutRecipientRepository payoutRecipientRepository;
    private final PayStackClient payStackClient;
    private final TransactionServiceImpl transactionService;



    public TransactionResponse requestPayOut(PayoutRequest payoutRequest) throws UserNotFound, InvalidAccount, InvalidAmount, InsufficientBalance, UnauthorizedAccess, IsNull, DuplicateTransaction, AccountDoesNotExist {

        PayoutRecipient recipient=createOrFetch(payoutRequest);
        TransferRequest transferRequest=mapToPayoutRequest(payoutRequest);
       return transactionService.initiateTransaction(transferRequest);

    }


    public PayoutRecipient createOrFetch(PayoutRequest payoutRequest) {

        Optional<PayoutRecipient> existing=payoutRecipientRepository.findByAccountNumberAndBankCode(payoutRequest.getAccountNumber(), payoutRequest.getBankCode());
   if (existing.isPresent()) {
       return existing.get();
   }

        Map<String,Object> body = new HashMap<>();
        body.put("type", "nuban");
        body.put("accountNumber", payoutRequest.getAccountNumber());
        body.put("bankCode", payoutRequest.getBankCode());
        body.put("name",payoutRequest.getAccountName());

        Map<String,Object> response=payStackClient.createRecipient(body);
        Map<String,Object> data=(Map<String,Object>)response.get("data");

        String code=(String)data.get("recipient_code");

        PayoutRecipient payoutRecipient = getPayoutRecipient(payoutRequest, code);
        return payoutRecipientRepository.save(payoutRecipient);

          }


}
