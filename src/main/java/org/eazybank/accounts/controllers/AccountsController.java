package org.eazybank.accounts.controllers;

import lombok.RequiredArgsConstructor;
import org.eazybank.accounts.dto.CustomerDto;
import org.eazybank.accounts.dto.ResponseDto;
import org.eazybank.accounts.service.IAccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.eazybank.accounts.constants.AccountsConstants.MESSAGE_201;
import static org.eazybank.accounts.constants.AccountsConstants.STATUS_201;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AccountsController {

    private final IAccountsService iAccountsService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(STATUS_201, MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

}
