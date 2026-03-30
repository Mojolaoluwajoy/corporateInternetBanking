package org.app.corporateinternetbanking.transaction.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactionRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.transaction.service.TransactionServiceImpl;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionServiceImpl service;

    @PostMapping("/initiate")
    public ResponseEntity<GenericResponse> initiateTransaction(@RequestBody TransactionRequest request, HttpServletRequest servletRequest) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess, DuplicateTransaction {
        log.info("URI: "+servletRequest.getRequestURI());
        TransactionResponse response= service.initiateTransaction(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Transaction successfully initiated...waiting for approval"), HttpStatus.OK);
    }
    @PostMapping("/approve")
    public ResponseEntity <GenericResponse> grantApproval(@RequestBody ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType, UserNotFound, UnauthorizedAccess, InvalidAmount, AccountDoesNotExist, CurrencyNotFound {
        ApprovalResponse response= service.approval(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Transaction Processed"),HttpStatus.OK);
    }
    @GetMapping("pending")
    public ResponseEntity<GenericResponse> viewPendingTransactions() throws NoPendingTransactionFound {
       List<TransactionResponse> response=service.viewPendingTransactions();
       return new ResponseEntity<>(GenericResponse.success(response,"Pending transactions found"),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transactions")
    public ResponseEntity<GenericResponse> getTransactions(@RequestParam int page,@RequestParam int size,@RequestParam (required = false) String status){
        return new ResponseEntity<>(GenericResponse.success(service.getTransactions(page,size, status),"Transaction found"),HttpStatus.OK);
    }


}
