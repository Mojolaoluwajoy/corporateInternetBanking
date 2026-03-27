package org.app.corporateinternetbanking.currency.dto;

import org.app.corporateinternetbanking.currency.enums.CurrencyStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyResponse {
    private String code;
    private String name;
    private String symbol;
    private CurrencyStatus status;

}
