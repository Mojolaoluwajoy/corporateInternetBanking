package org.app.corporateinternetbanking.account.utils.mapper;

import org.app.corporateinternetbanking.account.dto.AccountRequest;
import org.app.corporateinternetbanking.account.dto.AccountResponse;
import org.app.corporateinternetbanking.account.domain.entity.Account;
import org.app.corporateinternetbanking.currency.dto.CurrencyCodeDto;
import org.app.corporateinternetbanking.organization.dto.OrganizationId;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

import java.security.SecureRandom;

public class Map {

    private static String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        int number = 1000000000 + random.nextInt(900000000);
        return String.valueOf(number);

    }

    public static Account requestMap(AccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setName(request.getName());
        account.setType(request.getType());
        return account;
    }

    public static AccountResponse responseMap(Account account) {
        AccountResponse response = new AccountResponse();
        response.setName(account.getName());
        response.setName(account.getType());
        response.setCurrencyCode(new CurrencyCodeDto(account.getCurrency().getCode()));
        response.setOrganizationId(new OrganizationId(account.getOrganization().getId()));
        response.setAccountNumber(account.getAccountNumber());
        response.setCreatedBy(new UserIdDto(account.getCreatedBy().getId()));
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}
