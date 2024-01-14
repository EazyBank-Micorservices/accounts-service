package org.eazybank.accounts.service.impl;

import lombok.RequiredArgsConstructor;
import org.eazybank.accounts.dto.CustomerDto;
import org.eazybank.accounts.repository.AccountRepo;
import org.eazybank.accounts.repository.CustomerRepo;
import org.eazybank.accounts.service.IAccountsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IAccountsServiceImpl implements IAccountsService {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    @Override
    public void createAccount(CustomerDto customerDto) {

    }
}
