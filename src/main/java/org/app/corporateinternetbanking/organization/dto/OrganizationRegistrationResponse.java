package org.app.corporateinternetbanking.organization.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.organization.enums.OrganizationStatus;
import org.app.corporateinternetbanking.user.enums.Role;
import org.app.corporateinternetbanking.user.enums.UserStatus;

@Setter
@Getter
public class OrganizationRegistrationResponse {
   private Long id;
    private String name;
    private String registrationNumber;
private OrganizationStatus organizationStatus;


    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String nin;
    private OrganizationId organizationId;
    private Role role;
    private UserStatus status;

}
