package org.app.corporateinternetbanking.integration.paystack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(
        name = "payStackClient",
        url = "https://api.paystack.com",
        configuration=FeignConfiguration.class)

public interface PayStackClient {

    @PostMapping("/transaction/initialize")
    java.util.Map<String, Object> initializeTransaction(Map<String,Object> body);

   @PostMapping("/transferrecipient")
    java.util.Map<String, Object> createRecipient (Map<String,Object> body);

   @PostMapping("/transfer")
    java.util.Map<String, Object> initiateTransfer(Map<String,Object> body);


}
