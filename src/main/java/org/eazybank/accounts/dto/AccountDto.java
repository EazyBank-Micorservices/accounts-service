package org.eazybank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Account",
        description = "Schema to hold Account information"
)
public class AccountDto {

    @Schema(
            description = "Account number for EazyBank account",
            example = "7890567432"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @NotBlank(message = "Account Number cannot be null or empty")
    private Long accountNumber;

    @Schema(
            description = "Account type for EazyBank account",
            example = "Savings"
    )
    @NotBlank(message = "Account type cannot be null or empty")
    private String accountType;

    @Schema(
            description = "Eazy Bank branch Address",
            example = "123 Nairobi"
    )
    @NotBlank(message = "Branch Address cannot be null or empty")
    private String branchAddress;
}
