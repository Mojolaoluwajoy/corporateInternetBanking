package org.app.corporateinternetbanking.user.controller;

import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.user.dto.InvitationRequest;
import org.app.corporateinternetbanking.user.dto.UserResponse;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;
import org.app.corporateinternetbanking.user.service.UserService;
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

    @PostMapping("/invitation")
    public ResponseEntity<GenericResponse> sendInvitation(@RequestBody InvitationRequest  request)throws UserAlreadyRegistered, UnauthorizedAccess {
      String email=  service.sendInvitationTokenToUser(request);
        return new ResponseEntity<>(GenericResponse.success(email,"token successfully sent"), HttpStatus.CREATED);

    }

    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponse> viewAlUsers() {
        List<UserResponse> users = service.ViewAllUsers();
        return new ResponseEntity<>(GenericResponse.success(users, "Users found"), HttpStatus.OK);
    }
   @GetMapping("/users")
           public ResponseEntity<GenericResponse> getUsers(@RequestParam int page,@RequestParam int size,@RequestParam (required = false) String status){
       return new ResponseEntity<>(GenericResponse.success(service.viewByStatus(page,size,status),"Users found"),HttpStatus.OK);

    }
}
