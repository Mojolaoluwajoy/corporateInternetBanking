package org.app.corporateinternetbanking.currencyIntegration;

public class InvalidCurrency extends RuntimeException {
    public InvalidCurrency(String message) {
        super(message);
    }
}
