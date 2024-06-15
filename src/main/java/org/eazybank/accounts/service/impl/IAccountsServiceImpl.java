package org.eazybank.accounts.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eazybank.accounts.constants.AccountsConstants;
import org.eazybank.accounts.dto.AccountDto;
import org.eazybank.accounts.dto.AccountMessageDTO;
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
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class IAccountsServiceImpl implements IAccountsService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final StreamBridge streamBridge;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        customerRepo.findByMobileNumber(customer.getMobileNumber()).ifPresent(foundCustomer -> {
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number %s".formatted(customer.getMobileNumber()));
        });

        Customer savedCustomer = customerRepo.save(customer);
        Account savedAccount = accountRepo.save(createNewAccount(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);
    }

    private void sendCommunication(Account account, Customer customer) {
        AccountMessageDTO accountMessageDTO = new AccountMessageDTO(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending communication request for the details: {}", accountMessageDTO);
        boolean result = streamBridge.send("sendCommunication-out-0", accountMessageDTO);
        log.info("Is the communication request successfully triggered ?: {}", result);
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

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if (accountNumber != null) {
            Account account = accountRepo.findById(accountNumber)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account", "Account Number", accountNumber.toString())
                    );

            account.setCommunicationSw(true);
            accountRepo.save(account);
            isUpdated = true;
        }
        return isUpdated;
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
