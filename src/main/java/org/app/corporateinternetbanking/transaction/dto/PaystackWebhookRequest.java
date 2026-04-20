package org.app.corporateinternetbanking.transaction.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaystackWebhookRequest {
    private String event;
    private String data;


    public static class Data{
         private String reference;
         private  String status;
    }
}
