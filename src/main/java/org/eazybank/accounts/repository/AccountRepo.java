package org.eazybank.accounts.repository;

import org.eazybank.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByCustomerId(Long customerId);

    void deleteByCustomerId(Long customerId);
}
