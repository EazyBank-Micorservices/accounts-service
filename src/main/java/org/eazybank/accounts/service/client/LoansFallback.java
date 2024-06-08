package org.eazybank.accounts.service.client;

import org.eazybank.accounts.dto.LoanDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Qualifier("loansServiceFallBack")
public class LoansFallback implements LoansFeignClient {

    @Override
    public ResponseEntity<LoanDto> fetchLoanDetails(String mobileNumber) {
        return null;
    }
}
