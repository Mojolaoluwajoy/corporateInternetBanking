package org.app.corporateinternetbanking.transactionManagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.accountManagement.Account;
import org.app.corporateinternetbanking.transactionManagement.enums.TransactionStatus;
import org.app.corporateinternetbanking.transactionManagement.enums.TransactionType;

import java.math.BigDecimal;
@Setter
@Getter
public class TransactionResponse {
    private long id;
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private Account account;

}
