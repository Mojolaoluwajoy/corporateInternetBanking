package org.app.corporateinternetbanking.currency.service;

import org.app.corporateinternetbanking.currency.dto.CurrencyRequest;
import org.app.corporateinternetbanking.currency.dto.CurrencyResponse;
import org.app.corporateinternetbanking.currency.enums.CurrencyStatus;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.currency.model.Currency;
import org.app.corporateinternetbanking.currency.repository.CurrencyRepository;
import org.app.corporateinternetbanking.currency.utils.CurrencyMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{
    private final CurrencyRepository repository;


    @Override
    public CurrencyResponse changeCurrencyStatus(CurrencyRequest currencyRequest) throws CurrencyNotFound {
        Currency currency=repository.findByCode(currencyRequest.getCode())
                .orElseThrow(()-> new CurrencyNotFound("This currency does not exist"));
    if (currencyRequest.getStatus().equals(CurrencyStatus.ACTIVE)){
        currency.setStatus(CurrencyStatus.ACTIVE);
        repository.save(currency);
    } if (currencyRequest.getStatus().equals(CurrencyStatus.INACTIVE)){
        currency.setStatus(CurrencyStatus.INACTIVE);
   repository.save(currency);
    }
    return CurrencyMap.mapCurrencyResponse(currency);
    }
}
