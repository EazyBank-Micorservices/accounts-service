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

    /**
     * @param mobileNumber
     * @return
     */
    CustomerDto fetchAccount(String mobileNumber);


    /**
     * Update account boolean.
     *
     * @param customerDto the customer dto
     * @return the boolean
     */
    boolean updateAccount(CustomerDto customerDto);
}
