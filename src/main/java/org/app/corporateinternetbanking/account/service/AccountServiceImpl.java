package org.app.corporateinternetbanking.account.service;

import lombok.AllArgsConstructor;
import org.app.corporateinternetbanking.account.dto.AccountRequest;
import org.app.corporateinternetbanking.account.dto.AccountResponse;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.account.exception.UserNotFound;
import org.app.corporateinternetbanking.account.model.Account;
import org.app.corporateinternetbanking.account.repository.AccountRepository;
import org.app.corporateinternetbanking.currency.enums.CurrencyStatus;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotActive;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.currency.model.Currency;
import org.app.corporateinternetbanking.currency.repository.CurrencyRepository;
import org.app.corporateinternetbanking.email.EmailSenderService;
import org.app.corporateinternetbanking.organization.exceptions.OrganizationDoesNotExist;
import org.app.corporateinternetbanking.organization.model.Organization;
import org.app.corporateinternetbanking.organization.repository.OrganizationRepository;
import org.app.corporateinternetbanking.user.dto.UserIdDto;
import org.app.corporateinternetbanking.user.model.User;
import org.app.corporateinternetbanking.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.app.corporateinternetbanking.account.utils.Map.requestMap;
import static org.app.corporateinternetbanking.account.utils.Map.responseMap;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repository;
    @Autowired
    private EmailSenderService senderService;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    private final UserRepository userRepository;


    @Override
    public AccountResponse createAccount(AccountRequest request) throws OrganizationDoesNotExist, UserNotFound, CurrencyNotFound, CurrencyNotActive {
        Account account = requestMap(request);
        Currency currency=currencyRepository.findByCode(request.getCurrencyCode())
                .orElseThrow(()-> new CurrencyNotFound("This currency does not exist"));
        account.setCurrency(currency);
        if (currency.getStatus().equals(CurrencyStatus.INACTIVE)){
            throw new CurrencyNotActive("This currency is not available for use right now");

        }
        Organization organization = organizationRepository.findById(request.getOrganization())
                .orElseThrow(() -> new OrganizationDoesNotExist("Organization not found"));
        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new UserNotFound("The user with the specified id does not exist"));
        account.setOrganization(organization);
         account.setCreatedBy(user);

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
            response.setCreatedBy(new UserIdDto(savedAccount.getCreatedBy().getId()));
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
