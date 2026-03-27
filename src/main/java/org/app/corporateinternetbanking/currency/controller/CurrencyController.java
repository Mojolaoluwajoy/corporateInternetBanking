package org.app.corporateinternetbanking.currency.controller;

import org.app.corporateinternetbanking.currency.dto.CurrencyRequest;
import org.app.corporateinternetbanking.currency.dto.CurrencyResponse;
import org.app.corporateinternetbanking.currency.exceptions.CurrencyNotFound;
import org.app.corporateinternetbanking.currency.service.CurrencyService;
import org.app.corporateinternetbanking.dto.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

    @PostMapping("/status")
    public ResponseEntity<GenericResponse> changeStatus(@RequestBody CurrencyRequest request) throws CurrencyNotFound {
        CurrencyResponse response=currencyService.changeCurrencyStatus(request);
        return new ResponseEntity<>(GenericResponse.success(response,"Status successfully changed"), HttpStatus.OK);
    }
}
