package org.app.corporateinternetbanking.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.app.corporateinternetbanking.account.exception.AccountDoesNotExist;
import org.app.corporateinternetbanking.commons.response.GenericResponse;
import org.app.corporateinternetbanking.transaction.dto.PaystackWebhookRequest;
import org.app.corporateinternetbanking.transaction.exceptions.InvalidSignature;
import org.app.corporateinternetbanking.transaction.exceptions.TransactionDoesNotExist;
import org.app.corporateinternetbanking.transaction.service.WebhookPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookPaymentService webhookPaymentService;

    @PostMapping("/paystack")
    public ResponseEntity<GenericResponse> handleWebhook(@RequestBody PaystackWebhookRequest webhookRequest) throws TransactionDoesNotExist, AccountDoesNotExist, InvalidSignature {
        return new ResponseEntity<>(GenericResponse.success(webhookPaymentService.handleWebhook(webhookRequest),"Received"), HttpStatus.OK);
    }

}
