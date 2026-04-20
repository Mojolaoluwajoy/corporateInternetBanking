package org.app.corporateinternetbanking.transaction.utils.mapper;

import org.app.corporateinternetbanking.transaction.dto.FundRequest;
import org.app.corporateinternetbanking.transaction.dto.TransferRequest;
import org.app.corporateinternetbanking.transaction.enums.TransactionType;

public class FundingMap {

    public static TransferRequest mapToFundingRequest(FundRequest fundRequest) {
        TransferRequest transferRequest = new TransferRequest();

        transferRequest.setDestinationAccount(fundRequest.getAccountNumber());
        transferRequest.setAmount(fundRequest.getAmount());
        transferRequest.setCreatorId(fundRequest.getCreatedBy());
        transferRequest.setType(TransactionType.EXTERNAL_FUNDING);
        transferRequest.setIdempotencyKey(fundRequest.getIdempotencyKey());
        return transferRequest;

    }
}
