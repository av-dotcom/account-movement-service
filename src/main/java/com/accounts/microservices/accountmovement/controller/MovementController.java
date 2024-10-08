package com.accounts.microservices.accountmovement.controller;

import com.accounts.microservices.accountmovement.model.AccountReport;
import com.accounts.microservices.accountmovement.model.Movement;
import com.accounts.microservices.accountmovement.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementService movementService;

    @Autowired
    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<AccountReport>> getAccountReport(
            @RequestParam String cliente,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        LocalDate startDate = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_DATE);
        List<AccountReport> report = movementService.getAccountReport(cliente, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public List<Movement> getAllMovements() {
        return movementService.findAllMovements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movement> getMovementById(@PathVariable Long id) {
        Optional<Movement> movement = movementService.findMovementById(id);
        return movement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Movement> createMovement(@RequestBody Movement movement) {
        return new ResponseEntity<>(movementService.saveMovement(movement), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movement> updateMovement(@PathVariable Long id, @RequestBody Movement movement) {
        Optional<Movement> existingMovement = movementService.findMovementById(id);
        if (existingMovement.isPresent()) {
            movement.setId(id);
            return ResponseEntity.ok(movementService.saveMovement(movement));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
