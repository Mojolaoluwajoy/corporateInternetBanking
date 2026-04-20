package org.app.corporateinternetbanking.transaction.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.account.domain.entity.Account;
import org.app.corporateinternetbanking.account.domain.repository.AccountRepository;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.InvalidAccount;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.integration.currency.CurrencyExchangeService;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.ledger.service.LedgerService;
import org.app.corporateinternetbanking.transaction.domain.entity.Transaction;
import org.app.corporateinternetbanking.transaction.domain.repository.TransactionRepository;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransferRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;
import org.app.corporateinternetbanking.transaction.exceptions.*;
import org.app.corporateinternetbanking.transaction.utils.mapper.TransactionMap;
import org.app.corporateinternetbanking.user.domain.entity.User;
import org.app.corporateinternetbanking.user.domain.repository.UserRepository;
import org.app.corporateinternetbanking.user.enums.UserRole;
import org.app.corporateinternetbanking.user.exceptions.UnauthorizedAccess;
import org.app.corporateinternetbanking.user.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.app.corporateinternetbanking.transaction.utils.mapper.ApprovalMap.mapApprovalRequest;
import static org.app.corporateinternetbanking.transaction.utils.mapper.ApprovalMap.mapApprovalResponse;
import static org.app.corporateinternetbanking.transaction.utils.mapper.TransactionMap.mapRequest;
import static org.app.corporateinternetbanking.transaction.utils.mapper.TransactionMap.mapResponse;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public TransactionResponse initiateTransaction(TransferRequest request) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess, DuplicateTransaction, InsufficientBalance, InvalidAccount, IsNull {

        Transaction transaction = getTransaction(request);

        Result result = getResult(request);

        if (request.getAmount().compareTo(result.sourceAccount().getAvailableBalance()) > 0) {
            throw new InsufficientBalance("The balance must be greater than the amount to transfer");

        }

        switch (request.getType()){
            case INTERNAL_TRANSFER -> {
                validateInternalTransaction(request.getSourceAccount(), request.getDestinationAccount());
                transaction.setStatus(TransactionStatus.PENDING_APPROVAL);
            }

            case EXTERNAL_FUNDING -> {
                validateFunding(request.getDestinationAccount());
                transaction.setStatus(TransactionStatus.PENDING);
            }

            case EXTERNAL_PAYOUT -> {
                validatePayout(request.getSourceAccount());
            transaction.setStatus(TransactionStatus.PENDING_APPROVAL);
            }
          }
        result.sourceAccount().setAvailableBalance(result.sourceAccount().getTotalBalance().subtract(request.getAmount()));
        result.sourceAccount().setReservedBalance(result.sourceAccount().getReservedBalance().add(request.getAmount()));
        transaction.setSourceAccount(result.sourceAccount());
        transaction.setDestinationAccount(result.destinationAccount());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapResponse(savedTransaction);
    }




    private Result getResult(TransferRequest request) throws AccountDoesNotExist {
        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new AccountDoesNotExist("This source account does not exist"));
        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccount())
                .orElseThrow(() -> new AccountDoesNotExist("This destination account does not exist"));
        Result result = new Result(sourceAccount, destinationAccount);
        return result;
    }

    private record Result(Account sourceAccount, Account destinationAccount) {
    }

private void validateAccountNumber(String accountNumber) throws AccountDoesNotExist {
        Account account=accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountDoesNotExist("Account number does not exist"));
}
    private void validateInternalTransaction(String sourceAccount,String destinationAccount) throws InvalidAccount, IsNull, AccountDoesNotExist {
       if (sourceAccount.equals(null) || destinationAccount.equals(null)) {
           throw new IsNull("The source account and destination account cannot be null");
       }
        if (sourceAccount.equals(destinationAccount)) {
            throw new InvalidAccount("Cannot transfer to the same account");
        }
        validateAccountNumber(sourceAccount);
        validateAccountNumber(destinationAccount);
    }
    private void validateFunding(String destinationAccount) throws IsNull, AccountDoesNotExist {
        if (destinationAccount == null) {
            throw new IsNull("The destination account cannot be null");
        }
        validateAccountNumber(destinationAccount);
    }
     private void validatePayout(String sourceAccount) throws IsNull, AccountDoesNotExist {
        if (sourceAccount == null) {
            throw new IsNull("The source account cannot be null");
        }
        validateAccountNumber(sourceAccount);
    }

    
    private Transaction getTransaction(TransferRequest request) throws DuplicateTransaction, InvalidAmount, UserNotFound, UnauthorizedAccess {
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(request.getIdempotencyKey());

        if (existingTransaction.isPresent()) {
            throw new DuplicateTransaction("Duplicate transaction detected");
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmount("Amount must be greater than zero");
        }


        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserNotFound("This user does not exist"));

        if (!user.getRole().equals(UserRole.MAKER)) {
            throw new UnauthorizedAccess("the transaction creator must be a MAKER");
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmount("depositAmount must not be less than zero");
        }
        Transaction transaction = mapRequest(request);
        transaction.setCreatedBy(user);
        return transaction;
    }


    public Transaction findByTransactionReference(String transactionReference) throws TransactionDoesNotExist {
        return transactionRepository.findByIdempotencyKey(transactionReference)
                .orElseThrow(()-> new TransactionDoesNotExist("Transaction not found"));
    }


    @Override
    public List<TransactionResponse> viewPendingTransactions() throws NoPendingTransactionFound {

        List<Transaction> transactions = transactionRepository.findByStatus(TransactionStatus.PENDING);
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
        Pageable pageable = PageRequest.of(page, size);
        if (status != null) {
            return transactionRepository.findByStatus(status, pageable);
        }
        return transactionRepository.findAll(pageable);


    }

    @Transactional
    @Override
    public void expirePendingTransactions() {
        log.info("Processing pending orders");
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(24);
        List<Transaction> expiredTransactions = transactionRepository.findByStatusAndCreatedAtBefore(TransactionStatus.PENDING, expirationTime);

        for (Transaction transaction : expiredTransactions) {
            transaction.setStatus(TransactionStatus.EXPIRED);
            transaction.setProcessedAt(LocalDateTime.now());
            log.info("Automatically expired due to time-out");
        }

    }

    @Override
    public BigDecimal calculateTransactionVolume() {
        log.info("Calculating 24 hours window transaction volume");
        LocalDateTime end = LocalDateTime.now().minusHours(24);
        LocalDateTime start = LocalDateTime.now();
        List<Transaction> expiredTransactions = transactionRepository.findByCreatedAtBetweenAndStatus(start, end, TransactionStatus.SUCCESS);
        BigDecimal transactionVolume = BigDecimal.ZERO;
        for (Transaction transaction : expiredTransactions) {
            transactionVolume = transactionVolume.add(transaction.getAmount());
        }

        return transactionVolume;
    }

    @Override
    public void markSuccess(String reference) throws TransactionDoesNotExist {
        Transaction transaction = findByTransactionReference(reference);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
    }

    @Override
    public void markFailed(String reference) throws TransactionDoesNotExist {
        Transaction transaction=findByTransactionReference(reference);
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }


}
