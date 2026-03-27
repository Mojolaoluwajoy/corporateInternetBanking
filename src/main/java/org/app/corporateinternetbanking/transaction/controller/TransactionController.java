package org.app.corporateinternetbanking.transaction.controller;

import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.transaction.service.TransactionServiceImpl;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactiontRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
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
    public ResponseEntity<GenericResponse> initiateTransaction(@RequestBody TransactiontRequest request) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess {
        TransactionResponse response= service.initiateTransaction(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Transaction successfully initiated...waiting for approval"), HttpStatus.OK);
    }
    @PostMapping("/approve")
    public ResponseEntity <GenericResponse> grantApproval(@RequestBody ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType, UserNotFound, UnauthorizedAccess {
        ApprovalResponse response= service.approval(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Transaction Processed"),HttpStatus.OK);
    }


}
