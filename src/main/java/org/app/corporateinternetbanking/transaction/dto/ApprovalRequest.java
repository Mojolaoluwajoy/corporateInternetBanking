package org.app.corporateinternetbanking.transaction.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.transaction.enums.TransactionStatus;
@Setter
@Getter
public class ApprovalRequest {
    private long transactionId;
    private TransactionStatus status;
    private Long approverId;
}
