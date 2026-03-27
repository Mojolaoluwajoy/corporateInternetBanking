package org.app.corporateinternetbanking.account.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountRequest {
    private String name;
    private String type;
    private Long createdBy;
    private Long organization;
}
