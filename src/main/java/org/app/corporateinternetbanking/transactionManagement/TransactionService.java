package org.app.corporateinternetbanking.transactionManagement;

import org.app.corporateinternetbanking.accountManagement.AccountDoesNotExist;
import org.app.corporateinternetbanking.transactionManagement.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transactionManagement.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transactionManagement.dto.TransactionRequest;
import org.app.corporateinternetbanking.transactionManagement.dto.TransactionResponse;
import org.app.corporateinternetbanking.transactionManagement.exceptions.*;

public interface TransactionService {

        TransactionResponse initiateTransaction(TransactionRequest request) throws InvalidAmount, AccountDoesNotExist;

       ApprovalResponse approval(ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType;

}
