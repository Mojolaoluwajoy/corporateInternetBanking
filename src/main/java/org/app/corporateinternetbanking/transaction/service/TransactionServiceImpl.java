package org.app.corporateinternetbanking.transaction.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.account.domain.entity.Account;
import org.app.corporateinternetbanking.account.domain.repository.AccountRepository;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.currency.integration.CurrencyExchangeService;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.ledger.service.LedgerService;
import org.app.corporateinternetbanking.transaction.domain.entity.Transaction;
import org.app.corporateinternetbanking.transaction.domain.repository.TransactionRepository;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactionRequest;
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
    @Autowired
    CurrencyExchangeService currencyService;
    @Autowired
    LedgerService ledgerService;

    @Override
    public TransactionResponse initiateTransaction(TransactionRequest request) throws InvalidAmount, AccountDoesNotExist, UserNotFound, UnauthorizedAccess, DuplicateTransaction, InsufficientBalance {
        Optional<Transaction> existingTransaction = transactionRepository.findByTransactionReference(request.getTransactionReference());

        if (existingTransaction.isPresent()) {
            throw new DuplicateTransaction("Duplicate transaction detected");
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmount("Amount must be greater than zero");
        }

        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccount())
                .orElseThrow(() -> new AccountDoesNotExist("This destination account does not exist"));


        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserNotFound("This user does not exist"));

        if (!user.getRole().equals(UserRole.MAKER)) {
            throw new UnauthorizedAccess("the transaction creator must be a MAKER");
        }
        if (request.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new InvalidAmount("depositAmount must not be less than zero");
        }
        Transaction transaction = mapRequest(request);
        transaction.setCreatedBy(user);

        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new AccountDoesNotExist("This source account does not exist"));
        if (request.getAmount().compareTo(sourceAccount.getAvailableBalance()) > 0) {
            throw new InsufficientBalance("The balance must be greater than the amount to transfer");


        }
        sourceAccount.setAvailableBalance(sourceAccount.getTotalBalance().subtract(request.getAmount()));
        sourceAccount.setReservedBalance(sourceAccount.getReservedBalance().add(request.getAmount()));
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapResponse(savedTransaction);
    }


    @Override
    public ApprovalResponse approval(ApprovalRequest request) throws TransactionAlreadyProcessed, TransactionDoesNotExist, InvalidStatus, UnsupportedTransactionType, UserNotFound, UnauthorizedAccess, InvalidAmount, AccountDoesNotExist, CurrencyNotFound, InsufficientBalance {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new TransactionDoesNotExist("This transaction does not exist"));
        User user = userRepository.findById(request.getApproverId())
                .orElseThrow(() -> new UserNotFound("This user does not exist"));
        if (!user.getRole().equals(UserRole.APPROVER)) {
            throw new UnauthorizedAccess("The approve must be an APPROVER");
        }
        if (!transaction.getStatus().name().equalsIgnoreCase("PENDING")) {
            throw new TransactionAlreadyProcessed("This transaction has been processed");
        }
        if (request.getStatus() != TransactionStatus.SUCCESS && request.getStatus() != TransactionStatus.REJECTED) {
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
        processTransaction(transaction, TransactionType.INTERNAL_TRANSFER);
        return mapApprovalResponse(transaction);
    }

    private void processTransaction(Transaction transaction, TransactionType type) throws CurrencyNotFound {

    }

    private void processExternalPayout() {
    }

    private void processExternalFunding() {
    }

    private ApprovalResponse processInternalTransfer(Transaction transaction) throws CurrencyNotFound {
        Account source = transaction.getSourceAccount();
        Account destination = transaction.getDestinationAccount();
        BigDecimal amount = transaction.getAmount();
        if (source.getCurrency().getCode().equals(destination.getCurrency().getCode())) {
            BigDecimal newSourceBalance = source.getTotalBalance().subtract(amount);
            source.setTotalBalance(newSourceBalance);
            source.setReservedBalance(source.getReservedBalance().subtract(transaction.getAmount()));
        } else {
            String from = source.getCurrency().getCode();
            String to = destination.getCurrency().getCode();

            BigDecimal rate = currencyService.getRate(from, to);
            BigDecimal convertedAmount = amount.multiply(rate);

            BigDecimal newSourceBalance = source.getTotalBalance().subtract(amount);
            source.setTotalBalance(newSourceBalance);
            ledgerService.createEntry(source, transaction, EntryType.DEBIT, source.getCurrency().getCode(), amount, newSourceBalance);
            BigDecimal newDestinationBalance = destination.getTotalBalance().add(convertedAmount);
            destination.setTotalBalance(newDestinationBalance);
            ledgerService.createEntry(destination, transaction, EntryType.CREDIT, destination.getCurrency().getCode(), convertedAmount, newDestinationBalance);
            source.setReservedBalance(source.getReservedBalance().subtract(transaction.getAmount()));
            transaction.setConvertedAmount(convertedAmount);
            transaction.setExchangeRate(rate);
            transaction.setAmount(amount);

        }

        transaction.setType(TransactionType.INTERNAL_TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction = transactionRepository.save(transaction);
        return mapApprovalResponse(transaction);
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


}
