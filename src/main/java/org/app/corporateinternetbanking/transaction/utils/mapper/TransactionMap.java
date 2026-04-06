package org.app.corporateinternetbanking.transaction.utils.mapper;

import org.app.corporateinternetbanking.account.dto.AccountNumberDto;
import org.app.corporateinternetbanking.transaction.domain.entity.Transaction;
import org.app.corporateinternetbanking.transaction.dto.TransactionRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

public class TransactionMap {

    public static Transaction mapRequest(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionReference(request.getTransactionReference());

        return transaction;
    }

    public static TransactionResponse mapResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setSourceAccount(new AccountNumberDto(transaction.getSourceAccount().getAccountNumber()));
        response.setDestinationAccount(new AccountNumberDto(transaction.getDestinationAccount().getAccountNumber()));
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setStatus(transaction.getStatus());
        response.setTransactionReference(transaction.getTransactionReference());
        response.setCreator(new UserIdDto(transaction.getCreatedBy().getId()));
        return response;
    }
}
