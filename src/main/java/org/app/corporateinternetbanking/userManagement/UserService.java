package org.app.corporateinternetbanking.userManagement;

import org.app.corporateinternetbanking.organizationManagement.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.userManagement.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.userManagement.dto.UserResponse;
import org.app.corporateinternetbanking.userManagement.exceptions.NotAnAdminException;
import org.app.corporateinternetbanking.userManagement.exceptions.UserAlreadyRegistered;

import java.util.List;

public interface UserService {

    UserResponse register(UserRegistrationRequest request) throws UserAlreadyRegistered, NotAnAdminException, OrganizationDoesNotExist;

UserResponse createUser(UserRegistrationRequest request)throws UserAlreadyRegistered;

    List <UserResponse> ViewAllUsers();
}
