package org.app.corporateinternetbanking.transactionManagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.transactionManagement.enums.TransactionType;

import java.math.BigDecimal;
@Setter
@Getter
public class TransactionRequest {
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;

}
