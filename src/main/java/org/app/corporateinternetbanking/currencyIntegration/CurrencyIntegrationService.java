package org.app.corporateinternetbanking.currencyIntegration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CurrencyIntegrationService {
    private final  ExchangeRateClient exchangeRateClient;

    public BigDecimal getRate(String from,String to){
        return exchangeRateClient.getRate(from,to);
    }
    public BigDecimal convert(String from,String to,BigDecimal amount){
        BigDecimal rate=exchangeRateClient.getRate(from,to);
        return amount.multiply(rate);
    }
}
