package org.app.corporateinternetbanking.transactionManagement.exceptions;

public class TransactionAlreadyProcessed extends Exception {
    public TransactionAlreadyProcessed(String message) {
        super(message);
    }
}
