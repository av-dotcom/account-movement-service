package com.accounts.microservices.accountmovement.repository;

import com.accounts.microservices.accountmovement.model.Account;
import com.accounts.microservices.accountmovement.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountAndDateBetween(Account account, LocalDate startDate, LocalDate endDate);
}
