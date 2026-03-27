package org.app.corporateinternetbanking.user.controller;

import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.dto.LoginRequest;
import org.app.corporateinternetbanking.dto.LoginResponse;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.security.AuthenticationService;
import org.app.corporateinternetbanking.user.dto.SuperAdminResponse;
import org.app.corporateinternetbanking.user.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.user.exceptions.TokenExpiredOrInvalid;
import org.app.corporateinternetbanking.user.service.UserService;
import org.app.corporateinternetbanking.user.dto.SuperAdminRegistrationRequest;
import org.app.corporateinternetbanking.user.dto.UserResponse;
import org.app.corporateinternetbanking.user.exceptions.NotASuperAdminException;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
@Autowired
UserService service;
@Autowired
    AuthenticationService authenticationService;
    @PostMapping("/register")
public ResponseEntity<GenericResponse> registerSuperAdmin(@RequestBody SuperAdminRegistrationRequest request) throws UserAlreadyRegistered, NotASuperAdminException, OrganizationDoesNotExist {
SuperAdminResponse  response=service.registerSuperAdmin(request);
return new ResponseEntity<>(GenericResponse.success(response,"Super Admin registration successful"),HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticate(@RequestBody LoginRequest request){
        LoginResponse response=authenticationService.Authenticate(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Authentication successful"), HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createUser(@RequestBody UserRegistrationRequest request) throws UserAlreadyRegistered, NotASuperAdminException, OrganizationDoesNotExist, TokenExpiredOrInvalid {
        UserResponse response=service.createUserWithToken(request);
        return new ResponseEntity<>(GenericResponse.success(response,"user registration successful"), HttpStatus.CREATED);

    }


}
