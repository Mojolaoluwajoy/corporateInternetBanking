package org.app.corporateinternetbanking.transactionManagement.exceptions;

public class TransactionDoesNotExist extends Exception {
    public TransactionDoesNotExist(String message) {
        super(message);
    }
}
