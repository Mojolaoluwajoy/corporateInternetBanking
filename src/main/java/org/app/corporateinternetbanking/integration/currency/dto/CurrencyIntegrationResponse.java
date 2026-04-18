package org.app.corporateinternetbanking.integration.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CurrencyIntegrationResponse {
    private String base;
    @JsonProperty("conversion_rates")
    private Map<String,Double> conversionRates;
}
