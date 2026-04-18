package org.app.corporateinternetbanking.transaction.exceptions;

public class InvalidSignature extends Exception {
    public InvalidSignature(String message) {
        super(message);
    }
}
