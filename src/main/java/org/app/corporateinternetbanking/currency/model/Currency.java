package org.app.corporateinternetbanking.currency.model;

import org.app.corporateinternetbanking.currency.enums.CurrencyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private CurrencyStatus status=CurrencyStatus.INACTIVE;
}
