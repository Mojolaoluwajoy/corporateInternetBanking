package org.app.corporateinternetbanking.ledger.mode;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.ledger.enums.EntryType;
import org.app.corporateinternetbanking.transaction.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Account account;
    @ManyToOne
    private Transaction transaction;
    @Enumerated(EnumType.STRING)
    private EntryType type;
    private BigDecimal amount;
    private String currency;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt=LocalDateTime.now();
}
