package org.app.corporateinternetbanking.integration.currency;

import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.integration.currency.dto.CurrencyIntegrationResponse;
import org.app.corporateinternetbanking.integration.currency.mapper.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeService {

    @Autowired
    private CurrencyClient currencyClient;


    public BigDecimal getRate(String base, String target) throws CurrencyNotFound {
        CurrencyIntegrationResponse response = currencyClient.getRates(base);
        return CurrencyMapper.mapToRate(response, target);
    }
}
