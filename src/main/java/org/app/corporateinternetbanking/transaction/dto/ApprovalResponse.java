package org.app.corporateinternetbanking.transaction.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ApprovalResponse {
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType type;
private TransactionStatus status;
private UserIdDto approver;
private LocalDateTime approvedAt;
}
