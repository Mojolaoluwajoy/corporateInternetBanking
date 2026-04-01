package org.app.corporateinternetbanking.transaction.jobs;

import lombok.RequiredArgsConstructor;
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

    @Scheduled(fixedDelay =5000)
    public  void checkPendingTransactions() throws NoAdminsPresent {
        LocalDateTime threshold=LocalDateTime.now().minusMinutes(1);

        List<Transaction> pending=transactionRepository.findByStatusAndCreatedAtBefore(TransactionStatus.PENDING,threshold);
    User user=userRepository.findByRole(UserRole.ADMIN)
            .orElseThrow(()-> new NoAdminsPresent("There's no user with an admin role"));
   if (!pending.isEmpty()){
       String message=buildMessage(pending);
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
}