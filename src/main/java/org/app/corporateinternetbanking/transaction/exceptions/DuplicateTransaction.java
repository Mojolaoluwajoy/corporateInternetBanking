package org.app.corporateinternetbanking.transaction.exceptions;

public class DuplicateTransaction extends Exception {
    public DuplicateTransaction(String message) {
        super(message);
    }
}
