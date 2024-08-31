package com.accounts.microservices.accountmovement.service;

import com.accounts.microservices.accountmovement.model.Movement;
import com.accounts.microservices.accountmovement.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementService {
    private final MovementRepository movementRepository;

    @Autowired
    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public List<Movement> findAllMovements() {
        return movementRepository.findAll();
    }

    public Optional<Movement> findMovementById(Long id) {
        return movementRepository.findById(id);
    }

    public Movement saveMovement(Movement movement) {
        return movementRepository.save(movement);
    }

    public void deleteMovement(Long id) {
        movementRepository.deleteById(id);
    }
}
