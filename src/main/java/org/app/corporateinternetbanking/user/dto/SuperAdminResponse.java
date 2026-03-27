package org.app.corporateinternetbanking.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.user.enums.UserRole;
import org.app.corporateinternetbanking.user.enums.UserStatus;
@Setter
@Getter
public class SuperAdminResponse {
private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String nin;
    private UserRole role;
    private UserStatus status;

}
