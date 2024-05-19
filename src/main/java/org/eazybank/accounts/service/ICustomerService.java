package org.eazybank.accounts.service;

import org.eazybank.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
