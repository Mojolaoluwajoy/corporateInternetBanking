package org.app.corporateinternetbanking.currency.dto;

import org.app.corporateinternetbanking.currency.enums.CurrencyStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrencyRequest {
    private String code;
    private CurrencyStatus status;
}
