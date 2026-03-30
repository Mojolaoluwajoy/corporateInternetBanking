package org.app.corporateinternetbanking.integration.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CurrencyIntegrationResponse {
    private String base;
    private Map<String,Double> rates;
}
