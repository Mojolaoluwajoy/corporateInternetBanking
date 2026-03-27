package org.app.corporateinternetbanking.currency.service;

import org.app.corporateinternetbanking.currency.dto.CurrencyRequest;
import org.app.corporateinternetbanking.currency.dto.CurrencyResponse;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;

public interface CurrencyService {

    CurrencyResponse changeCurrencyStatus(CurrencyRequest currencyRequest) throws CurrencyNotFound;
}
