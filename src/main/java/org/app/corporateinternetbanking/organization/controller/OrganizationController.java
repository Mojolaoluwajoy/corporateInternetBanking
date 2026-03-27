package org.app.corporateinternetbanking.organization.controller;

import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.organization.dto.*;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationAlreadyProcessed;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.organization.service.OrganizationService;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService service;


    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createOrganization(@RequestBody OrganizationRequest request) throws UserAlreadyRegistered {
       OrganizationRegistrationResponse response=service.registerOrganization(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Organization registration complete,awaiting approval"), HttpStatus.OK);
    }

    @PostMapping("/approve")
    public ResponseEntity<GenericResponse> approveOrganization(@RequestBody ApprovalRequest request) throws UserAlreadyRegistered, UserNotFound, OrganizationAlreadyProcessed, OrganizationDoesNotExist {
      ApprovalResponse  response=service.processOrganizationRegistration(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Organization successfully processed"), HttpStatus.OK);
    }
    @PostMapping("/findBy")
    public ResponseEntity<GenericResponse> findById(@RequestBody Long id)throws OrganizationDoesNotExist {
        OrganizationOnlyResponse  response= null;

        response = service.viewById(id);

        return new ResponseEntity<>(GenericResponse.success(response,"Organization with the specified id found"),HttpStatus.OK);
    }
    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponse> viewAll(){
        List<OrganizationOnlyResponse> response=service.viewAll();
        return new ResponseEntity<>(GenericResponse.success(response,"Organization found!"),HttpStatus.OK);
    }
}
