package org.app.corporateinternetbanking.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PayoutRequest {

    private Long accountId;
    private BigDecimal amount;
    private String accountNumber;
    private String bankCode;
    private String bankName;
    private String accountName;
    private Long makerId;
    private String idempotencyKey;
}
