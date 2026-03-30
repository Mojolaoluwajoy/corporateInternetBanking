package org.app.corporateinternetbanking.currencyIntegration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
@Component
public class ExchangeRateClient {
    private final RestTemplate restTemplate=new RestTemplate();
    public BigDecimal getRate(String from,String to){
        try {
            String url="https://api.exchangerate-api.com/v4/latest/"+from;
//https://v6.exchangerate-api.com/v6/0b1d2d0491a30b594d716ea4/latest/USD
            ExchangeRateResponse response=restTemplate.getForObject(url,ExchangeRateResponse.class);
            if (response==null|| response.getRates().get(to)==null){
                throw new InvalidCurrency("Invalid currency or rate not found");
            }
            return response.getRates().get(to);
        }catch (Exception e){
            throw new RuntimeException("Fx service unavailable");
        }

    }
}
