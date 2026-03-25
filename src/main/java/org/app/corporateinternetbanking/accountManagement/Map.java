package org.app.corporateinternetbanking.accountManagement;

import org.app.corporateinternetbanking.accountManagement.dto.AccountRequest;
import org.app.corporateinternetbanking.accountManagement.dto.AccountResponse;
import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationIdDto;

import java.security.SecureRandom;

public class Map {

    private static String generateAccountNumber(){
        SecureRandom random=new SecureRandom();
       int number= 1000000000+random.nextInt(900000000);
       return String.valueOf(number);

    }
    public static Account requestMap(AccountRequest request){
        Account account=new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setName(request.getName());
        account.setType(request.getType());
        return account;
    }
    public static AccountResponse responseMap(Account account){
        AccountResponse response=new AccountResponse();
        response.setName(account.getName());
        response.setName(account.getType());
//        OrganizationId organizationId =new OrganizationId();
//       organizationId.setId(account.getOrganization().getId());
//       response.setOrganizationId(organizationId);
        response.setOrganizationId(new OrganizationIdDto(account.getOrganization().getId()));
      response.setAccountNumber(account.getAccountNumber());
        response.setCreatedBy(account.getCreatedBy());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}
