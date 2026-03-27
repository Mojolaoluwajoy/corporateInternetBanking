package org.app.corporateinternetbanking.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.user.enums.Role;
import org.app.corporateinternetbanking.user.enums.UserStatus;

@Setter
@Getter
public class SuperAdminRegistrationRequest {
    private String firstName;
    private String lastName;
     private String email;
      private String password;
      private String nin;
      private Role role;
      private UserStatus status;

}
