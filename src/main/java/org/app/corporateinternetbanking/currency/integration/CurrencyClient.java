package org.app.corporateinternetbanking.currency.integration;

import org.app.corporateinternetbanking.currency.integration.dto.CurrencyIntegrationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-client", url = "https://v6.exchangerate-api.com/v6/0b1d2d0491a30b594d716ea4/latest/")
public interface CurrencyClient {


    @GetMapping("/{base}")
    CurrencyIntegrationResponse getRates(@PathVariable String base);
}
