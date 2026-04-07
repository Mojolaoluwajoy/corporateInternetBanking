package org.app.corporateinternetbanking.currency.integration;

import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.currency.integration.dto.CurrencyIntegrationResponse;
import org.app.corporateinternetbanking.currency.integration.mapper.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class JsonPlaceRestClientService {

    @Autowired
    private CurrencyClient currencyClient;


    public BigDecimal getRate(String base, String target) throws CurrencyNotFound {
        CurrencyIntegrationResponse response = currencyClient.getRates(base);
        return CurrencyMapper.mapToRate(response, target);
    }
}
