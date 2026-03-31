package org.app.corporateinternetbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CorporateInternetBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorporateInternetBankingApplication.class, args);
    }

}
