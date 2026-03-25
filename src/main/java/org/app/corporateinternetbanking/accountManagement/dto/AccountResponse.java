package org.app.corporateinternetbanking.accountManagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.organizationManagement.dto.OrganizationIdDto;
import org.app.corporateinternetbanking.userManagement.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class AccountResponse {
     private String name;
     private String accountNumber;
     private BigDecimal balance;
     private String type;
     private OrganizationIdDto organizationId;
     private User createdBy;
    private LocalDateTime createdAt=LocalDateTime.now();


}
