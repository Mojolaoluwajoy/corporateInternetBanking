package org.app.corporateinternetbanking.transaction.utils;

import org.app.corporateinternetbanking.transaction.model.Transaction;
import org.app.corporateinternetbanking.transaction.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transaction.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transaction.dto.TransactionRequest;
import org.app.corporateinternetbanking.transaction.dto.TransactionResponse;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

import java.time.LocalDateTime;

public class Map {

    public static Transaction mapRequest(TransactionRequest request){
        Transaction transaction=new Transaction();

        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTransactionReference(request.getTransactionReference());

        return transaction;
    }
    public static Transaction mapApprovalRequest(ApprovalRequest request){
        Transaction transaction=new Transaction();
        transaction.setId(request.getTransactionId());
        transaction.setStatus(request.getStatus());
        return transaction;
    }
    public static ApprovalResponse mapApprovalResponse(Transaction transaction){
        ApprovalResponse approvalResponse=new ApprovalResponse();
        approvalResponse.setAccountNumber(transaction.getAccountNumber());
        approvalResponse.setAmount(transaction.getAmount());
        approvalResponse.setType(transaction.getType());
        approvalResponse.setStatus(transaction.getStatus());
        approvalResponse.setApprover(new UserIdDto(transaction.getApprovedBy().getId()));
        transaction.setApprovedAt(LocalDateTime.now());
        approvalResponse.setApprovedAt(transaction.getApprovedAt());
        return approvalResponse;
    }
    public static TransactionResponse mapResponse(Transaction transaction){
        TransactionResponse response=new TransactionResponse();
        response.setId(transaction.getId());
        response.setAccountNumber(transaction.getAccountNumber());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
       response.setStatus(transaction.getStatus());
       response.setAccount(transaction.getAccount());
       response.setTransactionReference(transaction.getTransactionReference());
       response.setCreator(new UserIdDto(transaction.getCreatedBy().getId()));
       return response;
    }
}
