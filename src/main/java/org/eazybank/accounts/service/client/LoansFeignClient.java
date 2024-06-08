package org.eazybank.accounts.service.client;

import org.eazybank.accounts.dto.LoanDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "loans", fallback = LoansFallback.class)
@Qualifier("loansServiceClient")
public interface LoansFeignClient {

    @GetMapping(value = "/api/fetch", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LoanDto> fetchLoanDetails(@RequestParam String mobileNumber);
}
