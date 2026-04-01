package org.app.corporateinternetbanking.transaction.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.corporateinternetbanking.notification.service.NotificationService;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.transaction.repository.TransactionRepository;
import org.app.corporateinternetbanking.transaction.service.TransactionService;
import org.app.corporateinternetbanking.user.enums.UserRole;
import org.app.corporateinternetbanking.user.exceptions.NoAdminsPresent;
import org.app.corporateinternetbanking.user.model.User;
import org.app.corporateinternetbanking.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionJobs {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final UserRepository  userRepository;
    private final NotificationService  notificationService;

    @Scheduled(fixedDelay = 5000)
    public void runExpirationJob() {
        transactionService.expirePendingTransactions();
    }

    @Scheduled(fixedDelayString ="${transaction,jobs.interval}")
    public  void checkPendingTransactions() throws NoAdminsPresent {
        log.info("checking pending transactions");
        LocalDateTime threshold=LocalDateTime.now().minusSeconds(10);

        List<Transaction> pending=transactionRepository.findByStatusAndCreatedAtBefore(TransactionStatus.PENDING,threshold);
    User user=userRepository.findByRole(UserRole.ADMIN)
            .orElseThrow(()-> new NoAdminsPresent("There's no user with an admin role"));
   if (!pending.isEmpty()){
       String message=buildMessage(pending);
       String adminEmail=user.getEmail();

       notificationService.createNotification(adminEmail,message);
   }
    }
    @Scheduled(fixedRateString ="${transaction,jobs.interval}")
    public  void buildDailyReports() throws NoAdminsPresent {
        log.info("building daily reports");
        LocalDateTime start=LocalDateTime.now().minusHours(24);
        LocalDateTime end=LocalDateTime.now();

        List<Transaction> createBetween=transactionRepository.findByCreatedAtBetween(start,end);
    User user=userRepository.findByRole(UserRole.ADMIN)
            .orElseThrow(()-> new NoAdminsPresent("There's no user with an admin role"));
   if (!createBetween.isEmpty()){
       String message=buildSummaryReportMessage();
       String adminEmail=user.getEmail();

       notificationService.createNotification(adminEmail,message);
   }
    }

    private String buildMessage(List<Transaction> transactions){

        StringBuilder message=new StringBuilder();
        message.append("Pending transaction alert\n\n");
        message.append("Total: ").append(transactions.size()).append("\n\n");

        for (Transaction transaction:transactions){
            message.append("ID: ").append(transaction.getId())
                    .append(" | Amount: ").append(transaction.getAmount())
                    .append(" | Created: ").append(transaction.getCreatedAt())
                    .append("\n");

        }
        return message.toString();
    }
    private String buildSummaryReportMessage(){

        StringBuilder message=new StringBuilder();
             log.info("sending daily operational report");
            LocalDateTime start=LocalDateTime.now();
        LocalDateTime end=LocalDateTime.now().minusHours(24);
        List<Transaction> transactions=transactionRepository.findByCreatedAtBetween(start,end);
        List<Transaction> pendingTransactions=transactionRepository.findByCreatedAtBetweenAndStatus(start,end,TransactionStatus.PENDING);
        List<Transaction> successfulTransactions=transactionRepository.findByCreatedAtBetweenAndStatus(start,end,TransactionStatus.APPROVED);
        List<Transaction> expiredTransactions=transactionRepository.findByCreatedAtBetweenAndStatus(start,end,TransactionStatus.EXPIRED);
        List<Transaction> rejectedTransactions=transactionRepository.findByCreatedAtBetweenAndStatus(start,end,TransactionStatus.REJECTED);
        message.append("Daily summary report\n\n");
        message.append("Total Transactions: ").append(transactions.size()).append("\n\n");
        message.append("Total pending transactions: ").append(pendingTransactions.size()).append("\n\n");
        message.append("Total successful transactions:").append(successfulTransactions.size()).append("\n\n");
        message.append("Total expired transactions: ").append(expiredTransactions.size()).append("\n\n");
        message.append("Total rejected transactions:").append(rejectedTransactions.size()).append("\n\n");
        return message.toString();
    }

}