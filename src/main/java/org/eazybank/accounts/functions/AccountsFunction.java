package org.eazybank.accounts.functions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eazybank.accounts.service.IAccountsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AccountsFunction {

    private final IAccountsService iAccountsService;


    @Bean
    public Consumer<Long> updateCommunication() {
        return accountNumber -> {
            log.info("updating communication status for the account number , {}", accountNumber);
            iAccountsService.updateCommunicationStatus(accountNumber);
        };
    }

}
