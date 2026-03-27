package org.app.corporateinternetbanking.transaction.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

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
private String transactionReference;
private UserIdDto creator;
}
