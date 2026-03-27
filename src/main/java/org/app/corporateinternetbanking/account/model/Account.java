package org.app.corporateinternetbanking.account.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.currency.model.Currency;
import org.app.corporateinternetbanking.organization.model.Organization;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column
            (unique = true)
    private String accountNumber;
    private BigDecimal balance=BigDecimal.ZERO;
    private String type;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
    @ManyToOne
    @JoinColumn(name ="currency" )
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
   @ManyToOne
   @JoinColumn(name = "created_by")
    private User createdBy;
   private LocalDateTime createdAt=LocalDateTime.now();
}
