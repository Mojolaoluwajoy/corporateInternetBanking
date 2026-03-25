package org.app.corporateinternetbanking.transaction.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;

import java.math.BigDecimal;

@Setter
@Getter
public class ApprovalResponse {
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;
private TransactionStatus status;
}
