package org.app.corporateinternetbanking.transactionManagement;

import org.app.corporateinternetbanking.accountManagement.AccountDoesNotExist;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.transactionManagement.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transactionManagement.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transactionManagement.dto.TransactionRequest;
import org.app.corporateinternetbanking.transactionManagement.dto.TransactionResponse;
import org.app.corporateinternetbanking.transactionManagement.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController("/transactions")
public class TransactionController {
    @Autowired
    TransactionServiceImpl service;

    @PostMapping("/initiate")
    public ResponseEntity<GenericResponse> initiateTransaction(@RequestBody TransactionRequest request) throws InvalidAmount, AccountDoesNotExist {
        TransactionResponse response= service.initiateTransaction(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Transaction successfully initiated...waiting for approval"), HttpStatus.OK);
    }
    @PostMapping("/approve")
    public ResponseEntity <GenericResponse> grantApproval(@RequestBody ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType {
        ApprovalResponse response= service.approval(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Transaction Processed"),HttpStatus.OK);
    }


}
