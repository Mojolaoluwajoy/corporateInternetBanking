package org.app.corporateinternetbanking.transaction.utils;

import org.app.corporateinternetbanking.account.dto.AccountNumberDto;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

public class ApprovalMap {
    public static Transaction mapApprovalRequest(ApprovalRequest request){
        Transaction transaction=new Transaction();
        transaction.setId(request.getTransactionId());
        transaction.setStatus(request.getStatus());
        return transaction;
    }
    public static ApprovalResponse mapApprovalResponse(Transaction transaction){
        ApprovalResponse approvalResponse=new ApprovalResponse();
        approvalResponse.setSourceAccount(new AccountNumberDto(transaction.getSourceAccount().getAccountNumber()));
        approvalResponse.setAmount(transaction.getAmount());
        approvalResponse.setExchangeRate(transaction.getExchangeRate());
        approvalResponse.setConvertedAmount(transaction.getConvertedAmount());
        approvalResponse.setType(transaction.getType());
        approvalResponse.setStatus(transaction.getStatus());
        approvalResponse.setApprover(new UserIdDto(transaction.getApprovedBy().getId()));
         approvalResponse.setApprovedAt(transaction.getApprovedAt());
        return approvalResponse;
    }
}
