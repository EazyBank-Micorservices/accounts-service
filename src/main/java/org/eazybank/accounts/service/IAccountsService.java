package org.eazybank.accounts.service;

import org.eazybank.accounts.dto.CustomerDto;

/**
 * The interface Accounts service.
 */
public interface IAccountsService {

    /**
     * Create account.
     *
     * @param customerDto the customer dto
     */
    void createAccount(CustomerDto customerDto);
}
