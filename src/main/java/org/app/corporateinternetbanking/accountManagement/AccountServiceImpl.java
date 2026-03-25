package org.app.corporateinternetbanking.accountManagement;

import org.app.corporateinternetbanking.accountManagement.dto.AccountRequest;
import org.app.corporateinternetbanking.accountManagement.dto.AccountResponse;
import org.app.corporateinternetbanking.organizationManagement.Organization;
import org.app.corporateinternetbanking.organizationManagement.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.organizationManagement.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.app.corporateinternetbanking.accountManagement.Map.requestMap;
import static org.app.corporateinternetbanking.accountManagement.Map.responseMap;

@org.springframework.stereotype.Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repository;
@Autowired
OrganizationRepository organizationRepository;

    @Override
    public AccountResponse createAccount(AccountRequest request) throws OrganizationDoesNotExist {
        Account account=requestMap(request);

        Organization organization= organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(()-> new OrganizationDoesNotExist("Organization not found"));
        account.setOrganization(organization);
Account savedAccount=repository.save(account);
return responseMap(savedAccount);
    }

    @Override
    public List<AccountResponse> viewAll() {
        List<Account> accounts=repository.findAll();
        List <AccountResponse> accountList=new ArrayList<>();
        for (Account savedAccount: accounts){
            AccountResponse response=new AccountResponse();
            response.setName(savedAccount.getName());
            response.setBalance(savedAccount.getBalance());
            response.setAccountNumber(savedAccount.getAccountNumber());
            response.setType(savedAccount.getType());
            response.setCreatedBy(savedAccount.getCreatedBy());
            response.setCreatedAt(savedAccount.getCreatedAt());
        }
        return accountList;
    }

    @Override
    public AccountResponse findById(Long id) throws AccountDoesNotExist {
      Optional<Account> account=repository.findById(id);
      if (account.isEmpty()){
          throw new AccountDoesNotExist("There's no account with the id entered");
      }
      return responseMap(account.get());

    }

}
