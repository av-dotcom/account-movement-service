package com.accounts.microservices.accountmovement.service;

import com.accounts.microservices.accountmovement.config.RabbitMQConfig;
import com.accounts.microservices.accountmovement.exception.InsufficientBalanceException;
import com.accounts.microservices.accountmovement.model.Account;
import com.accounts.microservices.accountmovement.model.AccountReport;
import com.accounts.microservices.accountmovement.model.Movement;
import com.accounts.microservices.accountmovement.repository.AccountRepository;
import com.accounts.microservices.accountmovement.repository.MovementRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MovementService(AccountRepository accountRepository, MovementRepository movementRepository, RabbitTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Movement> findAllMovements() {
        return movementRepository.findAll();
    }

    public Optional<Movement> findMovementById(Long id) {
        return movementRepository.findById(id);
    }

    public Movement saveMovement(Movement movement) {
        Account account = movement.getAccount();
        if (movement.getMovementType().equals("WITHDRAWAL") && account.getBalance().compareTo(BigDecimal.valueOf(movement.getAmount())) < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }
        Movement savedMovement = movementRepository.save(movement);
        rabbitTemplate.convertAndSend("account-movement-queue", savedMovement);
        return savedMovement;
    }

    public void deleteMovement(Long id) {
        movementRepository.deleteById(id);
    }

    public List<AccountReport> getAccountReport(String clientId, LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountRepository.findByClientId(clientId);
        return accounts.stream().map(account -> {
            List<Movement> movements = movementRepository.findByAccountAndDateBetween(account, startDate, endDate);
            return new AccountReport(account, movements);
        }).collect(Collectors.toList());
    }
}
