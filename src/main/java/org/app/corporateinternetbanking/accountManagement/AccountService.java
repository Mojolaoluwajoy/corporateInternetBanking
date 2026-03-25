package org.app.corporateinternetbanking.accountManagement;

import org.app.corporateinternetbanking.accountManagement.dto.AccountRequest;
import org.app.corporateinternetbanking.accountManagement.dto.AccountResponse;
import org.app.corporateinternetbanking.organizationManagement.OrganizationDoesNotExist;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request) throws OrganizationDoesNotExist;

   List <AccountResponse> viewAll();

   AccountResponse findById(Long id)throws AccountDoesNotExist;
}
