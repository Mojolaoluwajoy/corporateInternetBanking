package org.app.corporateinternetbanking.user.utils;

import org.app.corporateinternetbanking.user.dto.PasswordResetResponse;
import org.app.corporateinternetbanking.user.model.User;

public class PasswordResetResponseMap {

    public static PasswordResetResponse resetResponseMap(User user) {
        PasswordResetResponse response = new PasswordResetResponse();

        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return response;
    }
}
