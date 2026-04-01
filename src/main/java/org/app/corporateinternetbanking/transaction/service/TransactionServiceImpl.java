package org.app.corporateinternetbanking.transaction.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.account.repository.AccountRepository;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.integration.JsonPlaceRestClientService;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.ledger.service.LedgerService;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactionRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.transaction.repository.TransactionRepository;
import org.app.corporateinternetbanking.transaction.utils.TransactionMap;
import org.app.corporateinternetbanking.user.enums.UserRole;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.app.corporateinternetbanking.user.model.User;
import org.app.corporateinternetbanking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.app.corporateinternetbanking.transaction.utils.ApprovalMap.mapApprovalRequest;
import static org.app.corporateinternetbanking.transaction.utils.ApprovalMap.mapApprovalResponse;
import static org.app.corporateinternetbanking.transaction.utils.TransactionMap.mapRequest;
import static org.app.corporateinternetbanking.transaction.utils.TransactionMap.mapResponse;
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
   @Autowired
   AccountRepository accountRepository;
   @Autowired
   TransactionRepository transactionRepository;
@Autowired
    UserRepository userRepository;
@Autowired
JsonPlaceRestClientService currencyService;
@Autowired
    LedgerService ledgerService;

    @Override
    public TransactionResponse initiateTransaction(TransactionRequest request) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess, DuplicateTransaction, InsufficientBalance {
    Optional <Transaction> existingTransaction=transactionRepository.findByTransactionReference(request.getTransactionReference());

         if (existingTransaction.isPresent()){
            throw  new DuplicateTransaction("Duplicate transaction detected") ;
         }
if (request.getAmount().compareTo(BigDecimal.ZERO)<=0){
    throw new InvalidAmount("Amount must be greater than zero");
}

        Account destinationAccount=accountRepository.findByAccountNumber(request.getDestinationAccount())
                .orElseThrow(()->new AccountDoesNotExist("This destination account does not exist"));


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
        if (!request.getType().equals(TransactionType.CREDIT)) {

            Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccount())
                    .orElseThrow(() -> new AccountDoesNotExist("This source account does not exist"));
            // if (!request.getType().equals(TransactionType.CREDIT)) {
            if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientBalance("The balance must be greater than the amount to transfer");


            }
            transaction.setSourceAccount(sourceAccount);
        }
            transaction.setDestinationAccount(destinationAccount);

        Transaction savedTransaction=transactionRepository.save(transaction);

        return mapResponse(savedTransaction);
    }


    @Override
    public ApprovalResponse approval(ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType, UserNotFound, UnauthorizedAccess, InvalidAmount, AccountDoesNotExist, CurrencyNotFound, InsufficientBalance {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new TransactionDoesNotExist("This transaction does not exist"));
if (!transaction.getType().equals(TransactionType.CREDIT)) {
    if (transaction.getAmount().compareTo(transaction.getSourceAccount().getBalance()) < 0) {
        transaction.setStatus(TransactionStatus.REJECTED);
        throw new InsufficientBalance("The balance must be more than the amount to transfer");
    }
}
        User user = userRepository.findById(request.getApproverId())
                .orElseThrow(() -> new UserNotFound("This user does not exist"));
        if (!user.getRole().equals(UserRole.APPROVER)) {
            throw new UnauthorizedAccess("The approve must be an APPROVER");
        }
        if (!transaction.getStatus().name().equalsIgnoreCase("PENDING")) {
            throw new TransactionAlreadyProcessed("This transaction has been processed");
        }
        if (request.getStatus() != TransactionStatus.APPROVED && request.getStatus() != TransactionStatus.REJECTED) {
            throw new InvalidStatus("invalid status");
        }
        transaction.setProcessedBy(user);
        if (request.getStatus() == TransactionStatus.REJECTED) {
            transaction.setStatus(TransactionStatus.REJECTED);
            transactionRepository.save(transaction);
            return mapApprovalResponse(transaction);
        }
        transaction.setProcessedAt(LocalDateTime.now());
mapApprovalRequest(request);
        processTransaction(transaction);
        return mapApprovalResponse(transaction);
    }

    private void processTransaction(Transaction transaction) throws CurrencyNotFound {
        switch(transaction.getType()){
            case DEBIT -> processDebit(transaction);
            case CREDIT -> processCredit(transaction);
            case TRANSFER -> processTransfer(transaction);
        }
        transaction.setStatus(TransactionStatus.APPROVED);

    }

    private ApprovalResponse processTransfer(Transaction transaction) throws CurrencyNotFound {
        Account source=transaction.getSourceAccount();
        Account destination=transaction.getDestinationAccount();
        BigDecimal amount=transaction.getAmount();
        if (source.getCurrency().getCode().equals(destination.getCurrency().getCode())){
            BigDecimal newSourceBalance=source.getBalance().subtract(amount);
            BigDecimal newDestinationBalance=destination.getBalance().add(amount);
            source.setBalance(newSourceBalance);
            transaction.setUpdatedBalance(newSourceBalance);
        }
        else {
                  String from=source.getCurrency().getCode();
            String to=destination.getCurrency().getCode();

              BigDecimal rate=currencyService.getRate(from,to);
            BigDecimal convertedAmount=amount.multiply(rate);

             BigDecimal newSourceBalance=source.getBalance().subtract(amount);
            source.setBalance(newSourceBalance);
             ledgerService.createEntry(source,transaction, EntryType.DEBIT,source.getCurrency().getCode(),amount,newSourceBalance);
log.info("after debit");
            BigDecimal newDestinationBalance=destination.getBalance().add(convertedAmount);
            destination.setBalance(newDestinationBalance);
            ledgerService.createEntry(destination,transaction,EntryType.CREDIT,destination.getCurrency().getCode(),convertedAmount,newDestinationBalance);
            log.info("after debit");
              transaction.setUpdatedBalance(newSourceBalance);
            transaction.setConvertedAmount(convertedAmount);
            transaction.setExchangeRate(rate);
            transaction.setAmount(amount);

        }

        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.APPROVED);
        transaction=transactionRepository.save(transaction);
        return mapApprovalResponse(transaction);
    }

    private ApprovalResponse processCredit(Transaction transaction) {
        transaction.setType(TransactionType.CREDIT);
        Account destination=transaction.getDestinationAccount();
        BigDecimal newBalance = destination.getBalance().add(transaction.getAmount());
      destination.setBalance(newBalance);
         ledgerService.createEntry(destination,transaction,EntryType.CREDIT,destination.getCurrency().getCode(),transaction.getAmount(),destination.getBalance());

        accountRepository.save(destination);

        transaction.setUpdatedBalance(newBalance);
        transaction = transactionRepository.save(transaction);
        return   mapApprovalResponse(transaction);

    }

    private ApprovalResponse processDebit(Transaction transaction) {
     Account source=transaction.getSourceAccount();
      BigDecimal  newBalance = source.getBalance().subtract(transaction.getAmount());
       source.setBalance(newBalance);
       transaction.setType(TransactionType.DEBIT);
        ledgerService.createEntry(source,transaction,EntryType.DEBIT,source.getCurrency().getCode(),transaction.getAmount(),source.getBalance());

        accountRepository.save(source);

        transaction.setUpdatedBalance(newBalance);
        transaction = transactionRepository.save(transaction);
        return   mapApprovalResponse(transaction);

    }

    @Override
    public List<TransactionResponse> viewPendingTransactions() throws NoPendingTransactionFound {

        List <Transaction> transactions=transactionRepository.findByStatus(TransactionStatus.PENDING);
        if (transactions.isEmpty()) {
            throw new NoPendingTransactionFound("There's no pending transaction");
        }
              return transactions
                      .stream()
                      .map(TransactionMap::mapResponse)
                      .toList();
    }

    @Override
    public Page<Transaction> getTransactions(int page, int size, String status) {
        Pageable pageable= PageRequest.of(page,size);
        if (status!=null){
            return  transactionRepository.findByStatus(status,pageable);
        }
        return transactionRepository.findAll(pageable);


    }
@Transactional
    @Override
    public void expirePendingTransactions() {
       log.info("Processing pending orders");
        LocalDateTime expirationTime=LocalDateTime.now().minusMinutes(60);
        List <Transaction> expiredTransactions=transactionRepository.findByStatusAndCreatedAtBefore(TransactionStatus.PENDING,expirationTime);

        for (Transaction transaction:expiredTransactions){
            transaction.setStatus(TransactionStatus.EXPIRED);
            transaction.setProcessedAt(LocalDateTime.now());
            log.info("Automatically expired due to time-out");
                }

    }



}
