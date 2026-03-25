package org.app.corporateinternetbanking.transactionManagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.transactionManagement.enums.TransactionStatus;
@Setter
@Getter
public class ApprovalRequest {
    private long transactionId;
    private TransactionStatus status;
}
