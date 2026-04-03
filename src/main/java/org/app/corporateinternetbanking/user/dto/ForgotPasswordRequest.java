package org.app.corporateinternetbanking.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPasswordRequest {
    private String email;
    private String token;
    private String newPassword;
}
