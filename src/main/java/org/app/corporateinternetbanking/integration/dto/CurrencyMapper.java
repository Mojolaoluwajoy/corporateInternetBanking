package org.app.corporateinternetbanking.integration.dto;

import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;

import java.math.BigDecimal;

public class CurrencyMapper {

    public static BigDecimal mapToRate(CurrencyIntegrationResponse response,String target) throws CurrencyNotFound {
        Double rate=response.getConversionRates().get(target);

        if (rate==null){
            throw new CurrencyNotFound("invalid target currency");
        }
        return BigDecimal.valueOf(rate);
    }
}
