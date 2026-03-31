package org.app.corporateinternetbanking.integration;

import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.integration.dto.CurrencyIntegrationResponse;
import org.app.corporateinternetbanking.integration.dto.CurrencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
public class JsonPlaceRestClientService {

    @Autowired
    private RestClient restClient;

    private final String BASE_URL="https://v6.exchangerate-api.com/v6/0b1d2d0491a30b594d716ea4/latest/";

    public BigDecimal getRate(String base,String target) throws CurrencyNotFound {
        CurrencyIntegrationResponse response=restClient
                .get()
                .uri(BASE_URL + base)
                .retrieve()
                .body(CurrencyIntegrationResponse.class);
        return CurrencyMapper.mapToRate(response,target);
    }
}
