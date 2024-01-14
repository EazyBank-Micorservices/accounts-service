package org.eazybank.accounts.repository;

import org.eazybank.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account,Long> {
}
