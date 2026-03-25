package org.app.corporateinternetbanking.transaction.exceptions;

public class TransactionAlreadyProcessed extends Exception {
    public TransactionAlreadyProcessed(String message) {
        super(message);
    }
}
