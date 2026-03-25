package org.app.corporateinternetbanking.transactionManagement;

import org.app.corporateinternetbanking.transactionManagement.dto.ApprovalRequest;
import org.app.corporateinternetbanking.transactionManagement.dto.ApprovalResponse;
import org.app.corporateinternetbanking.transactionManagement.dto.TransactionRequest;
import org.app.corporateinternetbanking.transactionManagement.dto.TransactionResponse;

public class Map {

    public static Transaction mapRequest(TransactionRequest request){
        Transaction transaction=new Transaction();

        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());

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
       return response;
    }
}
