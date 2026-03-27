package org.app.corporateinternetbanking.transaction.service;

import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.account.repository.AccountRepository;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactiontRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.transaction.repository.TransactionRepository;
import org.app.corporateinternetbanking.user.enums.UserRole;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.app.corporateinternetbanking.user.model.User;
import org.app.corporateinternetbanking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static org.app.corporateinternetbanking.transaction.utils.TransactionMap.*;
import static org.app.corporateinternetbanking.transaction.utils.ApprovalMap.*;
@Service
public class TransactionServiceImpl implements TransactionService {
   @Autowired
   AccountRepository accountRepository;
   @Autowired
   TransactionRepository transactionRepository;
@Autowired
    UserRepository userRepository;


    @Override
    public TransactionResponse initiateTransaction(TransactiontRequest request) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess {
      Account sourceAccount= accountRepository.findByAccountNumber(request.getSourceAccount())
              .orElseThrow(()-> new AccountDoesNotExist("This source account does not exist"));
Account destinationAccount=accountRepository.findByAccountNumber(request.getDestinationAccount())
        .orElseThrow(()-> new AccountDoesNotExist("This destination account does not exist"));

        User user=userRepository.findById(request.getCreatorId())
        .orElseThrow(()->new UserNotFound("This user does not exist"));

if (!user.getRole().equals(UserRole.MAKER)){
    throw new UnauthorizedAccess("the transaction creator must be a MAKER");
}
        if (request.getAmount().compareTo(java.math.BigDecimal.ZERO)<0){
            throw new InvalidAmount("depositAmount must not be less than zero");
        }
        Transaction transaction=    mapRequest(request);
        transaction.setCreatedBy(user);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        Transaction savedTransaction=transactionRepository.save(transaction);

        return mapResponse(savedTransaction);
    }


    @Override
    public ApprovalResponse approval(ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType, UserNotFound, UnauthorizedAccess, InvalidAmount, AccountDoesNotExist {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new TransactionDoesNotExist("This transaction does not exist"));
        if(transaction.getAmount().compareTo(java.math.BigDecimal.ZERO)<0){
            transaction.setStatus(TransactionStatus.REJECTED);
            throw new InvalidAmount("Insufficient balance");
              }
User user=userRepository.findById(request.getApproverId())
        .orElseThrow(()-> new UserNotFound("This user does not exist"));
if (!user.getRole().equals(UserRole.APPROVER)){
    throw new UnauthorizedAccess("The approve must be an APPROVER");
}
        if (!transaction.getStatus().name().equalsIgnoreCase("PENDING")) {
            throw new TransactionAlreadyProcessed("This transaction has been processed");
        }
        if (request.getStatus()!= TransactionStatus.APPROVED && request.getStatus()!= TransactionStatus.REJECTED){
            throw new InvalidStatus("invalid status");
        }
        transaction.setApprovedBy(user);
        if (request.getStatus()==TransactionStatus.REJECTED){
            transaction.setStatus(TransactionStatus.REJECTED);
        transactionRepository.save(transaction);
        return mapApprovalResponse(transaction);
        }

        transaction = mapApprovalRequest(request);
       // Account account = transaction.getAccount();
        Account sourceAccount= accountRepository.findByAccountNumber(transaction.getSourceAccount().getAccountNumber())
                .orElseThrow(()-> new AccountDoesNotExist("This source account does not exist"));
        Account destinationAccount=accountRepository.findByAccountNumber(transaction.getDestinationAccount().getAccountNumber())
                .orElseThrow(()-> new AccountDoesNotExist("This destination account does not exist"));

        switch (transaction.getType()) {
            case CREDIT:
                BigDecimal newBalance = destinationAccount.getBalance().add(transaction.getAmount());
               destinationAccount.setBalance(newBalance);
                accountRepository.save(destinationAccount);

                transaction.setUpdatedBalance(newBalance);
                transaction = transactionRepository.save(transaction);
              return   mapApprovalResponse(transaction);


            case DEBIT:
                newBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
               sourceAccount.setBalance(newBalance);
                accountRepository.save(sourceAccount);

                transaction.setUpdatedBalance(newBalance);
                transaction = transactionRepository.save(transaction);
              return   mapApprovalResponse(transaction);

        }
        throw new UnsupportedTransactionType("Unsupported transaction type");
    }
}
