package org.app.corporateinternetbanking.transaction.exceptions;

public class NoPendingTransactionFound extends Exception {
    public NoPendingTransactionFound(String message) {
        super(message);
    }
}
