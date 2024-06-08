package org.eazybank.accounts.service.impl;

import org.eazybank.accounts.dto.AccountDto;
import org.eazybank.accounts.dto.CardDto;
import org.eazybank.accounts.dto.CustomerDetailsDto;
import org.eazybank.accounts.dto.LoanDto;
import org.eazybank.accounts.entity.Account;
import org.eazybank.accounts.entity.Customer;
import org.eazybank.accounts.exception.ResourceNotFoundException;
import org.eazybank.accounts.mapper.AccountMapper;
import org.eazybank.accounts.mapper.CustomerMapper;
import org.eazybank.accounts.repository.AccountRepo;
import org.eazybank.accounts.repository.CustomerRepo;
import org.eazybank.accounts.service.ICustomerService;
import org.eazybank.accounts.service.client.CardsFeignClient;
import org.eazybank.accounts.service.client.LoansFeignClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service

public class ICustomerServiceImpl implements ICustomerService {

    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    public ICustomerServiceImpl(AccountRepo accountRepo,
                                CustomerRepo customerRepo,
                                @Qualifier("cardsServiceClient") CardsFeignClient cardsFeignClient,
                                @Qualifier("loansServiceClient") LoansFeignClient loansFeignClient) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobile Number", mobileNumber)
        );
        Account account = accountRepo.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountDto(AccountMapper.mapToAccountsDto(account, new AccountDto()));

        ResponseEntity<LoanDto> loanDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        if (loanDtoResponseEntity != null) customerDetailsDto.setLoanDto(loanDtoResponseEntity.getBody());


        ResponseEntity<CardDto> cardDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        if (cardDtoResponseEntity != null) customerDetailsDto.setCardDto(cardDtoResponseEntity.getBody());

        return customerDetailsDto;
    }

}
