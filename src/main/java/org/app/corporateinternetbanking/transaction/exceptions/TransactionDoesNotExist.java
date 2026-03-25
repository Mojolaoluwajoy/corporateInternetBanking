package org.app.corporateinternetbanking.transaction.exceptions;

public class TransactionDoesNotExist extends Exception {
    public TransactionDoesNotExist(String message) {
        super(message);
    }
}
