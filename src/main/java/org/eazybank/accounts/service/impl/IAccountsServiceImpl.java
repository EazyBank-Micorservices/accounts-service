package org.eazybank.accounts.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eazybank.accounts.constants.AccountsConstants;
import org.eazybank.accounts.dto.AccountDto;
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
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class IAccountsServiceImpl implements IAccountsService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        customerRepo.findByMobileNumber(customer.getMobileNumber()).ifPresent(foundCustomer -> {
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number %s".formatted(customer.getMobileNumber()));
        });

        Customer savedCustomer = customerRepo.save(customer);
        accountRepo.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        return customerRepo
                .findByMobileNumber(mobileNumber)
                .map(customer -> {
                    CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
                    Account account = accountRepo
                            .findByCustomerId(customer.getCustomerId())
                            .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
                    customerDto.setAccountDto(AccountMapper.mapToAccountsDto(account, new AccountDto()));
                    return customerDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("customer", "mobile number", mobileNumber));

    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        return Optional.ofNullable(customerDto.getAccountDto()).map(accountDto -> {
            Account newAccount = accountRepo.findById(accountDto.getAccountNumber())
                    .map(account -> accountRepo.save(AccountMapper.mapToAccounts(accountDto, account)))
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "Account number", accountDto.getAccountNumber().toString()));

            customerRepo
                    .findById(newAccount.getCustomerId())
                    .map(customer -> customerRepo.save(CustomerMapper.mapToCustomer(customerDto, customer)))
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", newAccount.getCustomerId().toString()));

            return true;

        }).orElse(false);
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {

        return customerRepo.findByMobileNumber(mobileNumber).map(customer -> {
            accountRepo.deleteByCustomerId(customer.getCustomerId());
            customerRepo.deleteById(customer.getCustomerId());
            return true;
        }).orElse(false);
    }

    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setAccountType(AccountsConstants.SAVINGS);

        return newAccount;

    }
}
