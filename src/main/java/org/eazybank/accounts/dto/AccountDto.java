package org.eazybank.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountDto {
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @NotBlank(message = "Account Number cannot be null or empty")
    private Long accountNumber;
    @NotBlank(message = "Account type cannot be null or empty")
    private String accountType;
    @NotBlank(message = "Branch Address cannot be null or empty")
    private String branchAddress;
}
