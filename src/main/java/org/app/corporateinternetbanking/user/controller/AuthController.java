package org.app.corporateinternetbanking.user.controller;

import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.dto.LoginRequest;
import org.app.corporateinternetbanking.dto.LoginResponse;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.security.AuthenticationService;
import org.app.corporateinternetbanking.user.service.UserService;
import org.app.corporateinternetbanking.user.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.user.dto.UserResponse;
import org.app.corporateinternetbanking.user.exceptions.NotAnAdminException;
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
public ResponseEntity<GenericResponse> register(@RequestBody UserRegistrationRequest request) throws UserAlreadyRegistered, NotAnAdminException, OrganizationDoesNotExist {
UserResponse response=service.register(request);
return new ResponseEntity<>(GenericResponse.success(response,"Admin registration successful"),HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticate(@RequestBody LoginRequest request){
        LoginResponse response=authenticationService.Authenticate(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Authentication successful"), HttpStatus.OK);
    }


}
