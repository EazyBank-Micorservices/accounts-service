package org.eazybank.accounts.dto;

import lombok.Data;

@Data
public class AccountDto {
    private String accountNumber;
    private String accountType;
    private String branchAddress;
}
