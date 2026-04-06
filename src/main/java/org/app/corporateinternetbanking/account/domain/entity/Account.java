package org.app.corporateinternetbanking.account.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.app.corporateinternetbanking.currency.domain.entity.Currency;
import org.app.corporateinternetbanking.organization.domain.entity.Organization;
import org.app.corporateinternetbanking.transaction.domain.entity.Transaction;
import org.app.corporateinternetbanking.user.domain.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String accountNumber;
    private BigDecimal balance;
    private String type;
    @OneToMany(mappedBy = "sourceAccount")
    private List<Transaction> sourceTransactions;
    @OneToMany(mappedBy = "destinationAccount")
    private List<Transaction> destinationTransactions;
    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean flagged;

    @PrePersist
    public void prePersist() {
        if (balance == null) {
            balance = BigDecimal.valueOf(10000);
        }
    }

    private void onCreate() {
        this.flagged = false;
    }
}
