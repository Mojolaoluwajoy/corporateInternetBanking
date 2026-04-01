package org.app.corporateinternetbanking.transaction.exceptions;

public class InsufficientBalance extends Exception {
    public InsufficientBalance(String message) {
        super(message);
    }
}
