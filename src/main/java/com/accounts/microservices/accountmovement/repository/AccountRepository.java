package com.accounts.microservices.accountmovement.repository;

import com.accounts.microservices.accountmovement.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
