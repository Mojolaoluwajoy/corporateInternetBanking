package org.app.corporateinternetbanking.organization.controller;

import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.organization.service.OrganizationService;
import org.app.corporateinternetbanking.organization.dto.OrganizationRequest;
import org.app.corporateinternetbanking.organization.dto.OrganizationResponse;
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
    public ResponseEntity<GenericResponse> createAccount(@RequestBody OrganizationRequest request){
       OrganizationResponse response=service.registerOrganization(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Organization successfully registered"), HttpStatus.OK);
    }
    @PostMapping("/findBy")
    public ResponseEntity<GenericResponse> findById(@RequestBody Long id)throws OrganizationDoesNotExist {
        OrganizationResponse response= null;

        response = service.viewById(id);

        return new ResponseEntity<>(GenericResponse.success(response,"Organization with the specified id found"),HttpStatus.OK);
    }
    @GetMapping("/viewAll")
    public ResponseEntity<GenericResponse> viewAll(){
        List<OrganizationResponse> response=service.viewAll();
        return new ResponseEntity<>(GenericResponse.success(response,"Organization found!"),HttpStatus.OK);
    }
}
