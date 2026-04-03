package org.app.corporateinternetbanking.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.email.EmailSenderService;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.organization.model.Organization;
import org.app.corporateinternetbanking.organization.repository.OrganizationRepository;
import org.app.corporateinternetbanking.security.JwtService;
import org.app.corporateinternetbanking.user.dto.*;
import org.app.corporateinternetbanking.user.enums.UserStatus;
import org.app.corporateinternetbanking.user.exceptions.IncorrectPassword;
import org.app.corporateinternetbanking.user.exceptions.InvalidEmail;
import org.app.corporateinternetbanking.user.exceptions.TokenExpiredOrInvalid;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;
import org.app.corporateinternetbanking.user.model.User;
import org.app.corporateinternetbanking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.app.corporateinternetbanking.user.utils.Map.mapResponse;
import static org.app.corporateinternetbanking.user.utils.Map.userMapRequest;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailSenderService senderService;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public String sendInvitationTokenToUser(InvitationRequest invitationRequest) {
        String token = jwtService.generateEmailToken(invitationRequest.getUserEmail());
        sendMail(invitationRequest, token);
        return invitationRequest.getUserEmail();
    }

    @Override
    public UserResponse createUserWithToken(UserRegistrationRequest request) throws UserAlreadyRegistered, OrganizationDoesNotExist, TokenExpiredOrInvalid {
        if (!jwtService.isEmailTokenValid(request.getToken())) {
            throw new TokenExpiredOrInvalid("Token expired or its invalid");
        }
        String nin = request.getNin();
        String email = request.getEmail();
        if (repository.findByNin(nin).isPresent() || repository.findByEmail(email).isPresent()) {
            throw new UserAlreadyRegistered("This user already exists");
        }
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new OrganizationDoesNotExist("This organization does not exist"));
        User user = userMapRequest(request);
        user.setOrganization(organization);
        user.setStatus(UserStatus.ACTIVE);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        User savedUser = repository.save(user);
        return mapResponse(savedUser);
    }

    @Override
    public List<UserResponse> ViewAllUsers() {
        List<User> users = repository.findAll();
        List<UserResponse> userList = new ArrayList<>();
        for (User savedUser : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(savedUser.getUserId());
            userResponse.setFirstName(savedUser.getFirstName());
            userResponse.setLastName(savedUser.getLastName());
            userResponse.setNin(savedUser.getNin());
            userResponse.setEmail(savedUser.getEmail());
            userResponse.setRole(savedUser.getRole());
            userResponse.setStatus(savedUser.getStatus());

        }
        return userList;
    }

    @Override
    public Page<User> viewByStatus(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        if (status != null) {
            return repository.findByStatus(status, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public String resetPassword(PasswordResetRequest passwordResetRequest) throws IncorrectPassword {
        String oldPassword = passwordEncoder.encode(passwordResetRequest.getOldPassword());
        User user = repository.findByPassword(oldPassword)
                .orElseThrow(() -> new IncorrectPassword("The old password you entered is incorrect"));
        if (user.getPassword().equals(oldPassword)) {
            String newPassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
            user.setPassword(newPassword);
            repository.save(user);
        }
        return "Your request is being processed";
    }

    @Override
    public String resetForgottenPassword(ForgotPasswordRequest forgotPasswordRequest) throws InvalidEmail, TokenExpiredOrInvalid {
        User user = repository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new InvalidEmail("Email not found"));
        if (!jwtService.isEmailTokenValid(forgotPasswordRequest.getToken())) {
            throw new TokenExpiredOrInvalid("Token expired or its invalid");
        }
        user.setPassword(forgotPasswordRequest.getNewPassword());
        repository.save(user);
        return "Password reset successful";
    }


    @Override
    public String sendForgotPasswordToken(String email) throws InvalidEmail {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidEmail("Email not found"));
        String token = jwtService.generateEmailToken(email);
        senderService.sendEmail(user.getEmail(), "Password reset token", "Your password reset token is: \n" + token);
        return "Check your email";
    }


    public void sendMail(InvitationRequest invitationRequest, String token) {

        senderService.sendEmail(invitationRequest.getUserEmail(), "Account Creation Token", "Your verification token is: \n" + token);


    }
}
