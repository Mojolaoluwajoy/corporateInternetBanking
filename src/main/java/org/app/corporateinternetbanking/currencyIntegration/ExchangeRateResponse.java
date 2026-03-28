package org.app.corporateinternetbanking.currencyIntegration;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
@Setter
@Getter
public class ExchangeRateResponse {
    private String base;
    private Map<String, BigDecimal> rates;
}
