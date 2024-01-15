package org.eazybank.accounts.service.impl;

import lombok.RequiredArgsConstructor;
import org.eazybank.accounts.constants.AccountsConstants;
import org.eazybank.accounts.dto.CustomerDto;
import org.eazybank.accounts.entity.Account;
import org.eazybank.accounts.entity.Customer;
import org.eazybank.accounts.exception.CustomerAlreadyExistsException;
import org.eazybank.accounts.exception.ResourceNotFoundException;
import org.eazybank.accounts.mapper.AccountMapper;
import org.eazybank.accounts.mapper.CustomerMapper;
import org.eazybank.accounts.repository.AccountRepo;
import org.eazybank.accounts.repository.CustomerRepo;
import org.eazybank.accounts.service.IAccountsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class IAccountsServiceImpl implements IAccountsService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto);

        customerRepo.findByMobileNumber(customer.getMobileNumber()).ifPresent(foundCustomer -> {
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number %s".formatted(customer.getMobileNumber()));
        });

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("user 123");
        Customer savedCustomer = customerRepo.save(customer);
        accountRepo.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        return customerRepo
                .findByMobileNumber(mobileNumber)
                .map(customer -> {
                    CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer);
                    Account account = accountRepo
                            .findByCustomerId(customer.getCustomerId())
                            .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
                    customerDto.setAccountDto(AccountMapper.mapToAccountsDto(account));
                    return customerDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("customer", "mobile number", mobileNumber));

    }

    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setAccountType(AccountsConstants.SAVINGS);

        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("user 123");

        return newAccount;

    }
}
