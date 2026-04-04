package org.app.corporateinternetbanking.user.service;

import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.user.dto.*;
import org.app.corporateinternetbanking.user.exceptions.IncorrectPassword;
import org.app.corporateinternetbanking.user.exceptions.InvalidEmail;
import org.app.corporateinternetbanking.user.exceptions.TokenExpiredOrInvalid;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;
import org.app.corporateinternetbanking.user.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {


    UserResponse createUserWithToken(UserRegistrationRequest request) throws UserAlreadyRegistered, TokenExpiredOrInvalid, OrganizationDoesNotExist;

    List<UserResponse> ViewAllUsers();

    String sendInvitationTokenToUser(InvitationRequest request);


    Page<User> viewByStatus(int page, int size, String status);

    PasswordResetResponse resetPassword(PasswordResetRequest passwordResetRequest) throws IncorrectPassword, InvalidEmail;

    String resetForgottenPassword(ForgotPasswordRequest forgotPasswordRequest) throws InvalidEmail, TokenExpiredOrInvalid;

    PasswordResetResponse sendForgotPasswordToken(String email) throws InvalidEmail;

}
