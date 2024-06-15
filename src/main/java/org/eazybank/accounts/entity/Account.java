package org.eazybank.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    private Long customerId;
    @Id
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
    @Column(name = "communication_sw")
    private Boolean communicationSw;
}
