package org.eazybank.accounts.mapper;

import org.eazybank.accounts.dto.AccountDto;
import org.eazybank.accounts.entity.Account;

public class AccountMapper {
    public static AccountDto mapToAccountsDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(String.valueOf(account.getAccountNumber()));
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBranchAddress(accountDto.getBranchAddress());
        return accountDto;
    }

    public static Account mapToAccounts(AccountDto accountDto) {
        Account account = new Account();
        account.setAccountNumber(Long.valueOf(accountDto.getAccountNumber()));
        account.setAccountType(accountDto.getAccountType());
        account.setBranchAddress(accountDto.getBranchAddress());
        return account;
    }
}
