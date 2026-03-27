package org.app.corporateinternetbanking.account.service;

import org.app.corporateinternetbanking.account.dto.AccountRequest;
import org.app.corporateinternetbanking.account.dto.AccountResponse;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request) throws OrganizationDoesNotExist, UserNotFound;

   List <AccountResponse> viewAll();

   AccountResponse findById(Long id)throws AccountDoesNotExist;
}
