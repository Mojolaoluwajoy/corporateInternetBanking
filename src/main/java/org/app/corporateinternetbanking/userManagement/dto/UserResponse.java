package org.app.corporateinternetbanking.userManagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationIdDto;
import org.app.corporateinternetbanking.userManagement.enums.Role;
import org.app.corporateinternetbanking.userManagement.enums.Status;

@Setter
@Getter
public class UserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String nin;
    private OrganizationIdDto organizationId;
    private Role role;
    private Status status;

}
