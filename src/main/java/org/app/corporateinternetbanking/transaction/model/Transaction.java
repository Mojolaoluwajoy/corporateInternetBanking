package org.app.corporateinternetbanking.transaction.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Transaction {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String accountNumber;
   private BigDecimal amount;
   @EnumeratedValue
   private TransactionStatus status=TransactionStatus.PENDING;
   @EnumeratedValue
   private TransactionType type;
   @ManyToOne
   @JoinColumn(name = "accountI_id")
   private Account account;
   private LocalDateTime localDateTime=LocalDateTime.now();
   private BigDecimal updatedBalance=BigDecimal.ZERO;
}
