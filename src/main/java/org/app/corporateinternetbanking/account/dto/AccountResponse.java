package org.app.corporateinternetbanking.account.dto;

import lombok.Getter;
import lombok.Setter;
import org.app.corporateinternetbanking.organization.dto.OrganizationId;
import org.app.corporateinternetbanking.user.dto.UserIdDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class AccountResponse {
     private String name;
     private String accountNumber;
     private BigDecimal balance;
     private String type;
     private OrganizationId organizationId;
     private UserIdDto createdBy;
    private LocalDateTime createdAt=LocalDateTime.now();


}
