package org.app.corporateinternetbanking.accountManagement;

import org.app.corporateinternetbanking.accountManagement.dto.AccountRequest;
import org.app.corporateinternetbanking.accountManagement.dto.AccountResponse;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.organizationManagement.OrganizationDoesNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    AccountServiceImpl service;
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createAccount(@RequestBody AccountRequest request) throws OrganizationDoesNotExist {
        AccountResponse response=service.createAccount(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Account successfully registered"), HttpStatus.OK);
    }
    @PostMapping("/find")
    public ResponseEntity<GenericResponse> findById(@RequestBody Long id)throws AccountDoesNotExist{
        AccountResponse response= null;

            response = service.findById(id);

        return new ResponseEntity<>(GenericResponse.success(response,"Account with the specified id found"),HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<GenericResponse> viewAll(){
     List<AccountResponse> response=service.viewAll();
     return new ResponseEntity<>(GenericResponse.success(response,"Accounts found!"),HttpStatus.OK);
    }
}
