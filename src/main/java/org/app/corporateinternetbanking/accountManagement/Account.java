package org.app.corporateinternetbanking.accountManagement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.organizationManagement.Organization;
import org.app.corporateinternetbanking.transactionManagement.Transaction;
import org.app.corporateinternetbanking.userManagement.User;

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
    @JoinColumn(name = "organization_id")
    private Organization organization;
   @ManyToOne
 @JoinColumn(name = "created_by")
    private User createdBy;
   private LocalDateTime createdAt=LocalDateTime.now();
}
