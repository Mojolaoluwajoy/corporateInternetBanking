package org.app.corporateinternetbanking.currency.model;

import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.currency.enums.CurrencyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String code;
    private String symbol;
    @Enumerated (EnumType.STRING)
    private CurrencyStatus status;
    @OneToMany(mappedBy = "currency")
    private List<Account> accounts;
    }

//INSERT INTO currency(name,code,symbol,status) VALUES
//('Nigerian Naira', 'NGN', '#','INACTIVE'),
//        ('US Dollar','USD','$','INACTIVE'),
//        ('Euro','EUR','E','INACTIVE'),
//        ('Canadian Dollar','CAD','C$','INACTIVE'),
//        ('Australian Dollar','AUD','A$','INACTIVE');