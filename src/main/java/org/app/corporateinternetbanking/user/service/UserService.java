package org.app.corporateinternetbanking.user.service;

import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.user.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.user.dto.UserResponse;
import org.app.corporateinternetbanking.user.exceptions.NotAnAdminException;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;

import java.util.List;

public interface UserService {

    UserResponse register(UserRegistrationRequest request) throws UserAlreadyRegistered, NotAnAdminException, OrganizationDoesNotExist;

UserResponse createUser(UserRegistrationRequest request)throws UserAlreadyRegistered;

    List <UserResponse> ViewAllUsers();
}
