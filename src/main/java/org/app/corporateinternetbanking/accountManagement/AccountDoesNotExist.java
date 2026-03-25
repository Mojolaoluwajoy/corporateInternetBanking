package org.app.corporateinternetbanking.accountManagement;

public class AccountDoesNotExist extends Exception {
    public AccountDoesNotExist(String message) {
        super(message);
    }
}
