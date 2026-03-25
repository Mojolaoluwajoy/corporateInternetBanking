package org.app.corporateinternetbanking.userManagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.userManagement.enums.Role;
import org.app.corporateinternetbanking.userManagement.enums.Status;

@Setter
@Getter
public class UserRegistrationRequest {
    private String firstName;
    private String lastName;
     private String email;
      private String password;
      private String nin;
      private String adminKey;
      private Long organizationId;
   private Role role;
   private Status status;

}
