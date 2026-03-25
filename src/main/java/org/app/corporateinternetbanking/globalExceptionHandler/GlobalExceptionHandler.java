package org.app.corporateinternetbanking.globalExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.user.exceptions.NotAnAdminException;
import org.app.corporateinternetbanking.user.exceptions.UserAlreadyRegistered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(UserAlreadyRegistered.class)
    public ResponseEntity<GenericResponse> handleUserAlreadyRegistered(UserAlreadyRegistered registered){
    log.error("Error registering user: {}",registered.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(registered.getMessage()), HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(NotAnAdminException.class)
    public ResponseEntity<GenericResponse> handleNotAnAdminException(NotAnAdminException exception){
    log.error("Not an admin: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(AccountDoesNotExist.class)
    public ResponseEntity<GenericResponse> handleAccountDoesNotExist(AccountDoesNotExist exception){
    log.error("Account not found: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(OrganizationDoesNotExist.class)
    public ResponseEntity<GenericResponse> handleOrganizationDoesNotExist(OrganizationDoesNotExist exception){
    log.error("Organization not found: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(InvalidAmount.class)
    public ResponseEntity<GenericResponse> handleInvalidAmount(InvalidAmount exception){
    log.error("Invalid amount entered: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(InvalidStatus.class)
    public ResponseEntity<GenericResponse> handleInvalidStatus(InvalidStatus exception){
    log.error("Invalid status entered: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(TransactionAlreadyProcessed.class)
    public ResponseEntity<GenericResponse> handleTransactionAlreadyProcessed(TransactionAlreadyProcessed exception){
    log.error("Transaction already processed: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(TransactionDoesNotExist.class)
    public ResponseEntity<GenericResponse> handleTransactionDoesNotExist(TransactionDoesNotExist exception){
    log.error("Transaction does not exist: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(UnsupportedTransactionType.class)
    public ResponseEntity<GenericResponse> handleUnsupportedTransactionType(UnsupportedTransactionType exception){
    log.error("Unsupported transaction type: {}",exception.getMessage());
    return new ResponseEntity<>(GenericResponse.failed(exception.getMessage()), HttpStatus.BAD_REQUEST);
}


}
