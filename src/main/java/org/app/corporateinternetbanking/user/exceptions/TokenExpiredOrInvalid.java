package org.app.corporateinternetbanking.user.exceptions;

public class TokenExpiredOrInvalid extends Exception {
    public TokenExpiredOrInvalid(String message) {
        super(message);
    }
}
