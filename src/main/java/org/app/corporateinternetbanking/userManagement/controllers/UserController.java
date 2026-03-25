package org.app.corporateinternetbanking.userManagement.controllers;

import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.userManagement.UserService;
import org.app.corporateinternetbanking.userManagement.dto.UserRegistrationRequest;
import org.app.corporateinternetbanking.userManagement.dto.UserResponse;
import org.app.corporateinternetbanking.userManagement.exceptions.NotAnAdminException;
import org.app.corporateinternetbanking.userManagement.exceptions.UserAlreadyRegistered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService service;
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createUser(@RequestBody UserRegistrationRequest request)throws UserAlreadyRegistered, NotAnAdminException {
        UserResponse response=service.createUser(request);
        return new ResponseEntity<>(GenericResponse.success(response,"user registration successful"), HttpStatus.CREATED);

    }
    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponse> viewAlUsers(){
        List<UserResponse> users=service.ViewAllUsers();
        return new ResponseEntity<>(GenericResponse.success(users,"Users found"),HttpStatus.OK);
    }
}
