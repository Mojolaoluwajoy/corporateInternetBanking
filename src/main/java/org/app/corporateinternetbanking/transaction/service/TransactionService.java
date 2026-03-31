package org.app.corporateinternetbanking.transaction.service;

import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactionRequest;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

        TransactionResponse initiateTransaction(TransactionRequest request) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess, DuplicateTransaction;

       ApprovalResponse approval(ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType, UserNotFound, UnauthorizedAccess, InvalidAmount, AccountDoesNotExist, CurrencyNotFound;

       List<TransactionResponse> viewPendingTransactions() throws NoPendingTransactionFound;
       Page<Transaction> getTransactions(int page,int size,String status);


       void expirePendingTransactions();
}
