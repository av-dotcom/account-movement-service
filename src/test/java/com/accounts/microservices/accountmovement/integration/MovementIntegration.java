package com.accounts.microservices.accountmovement.integration;

import com.accounts.microservices.accountmovement.model.Account;
import com.accounts.microservices.accountmovement.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class MovementIntegration {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenRegisterMovement_thenUpdateAccountBalance() throws Exception {
        Account account = new Account("478758", "AHORROS", 1000.0, true);
        accountRepository.save(account);

        mockMvc.perform(post("/api/movements")
            .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\": \"478758\", \"movementType\": \"DEPOSIT\", \"amount\": 200.0, \"balance\": 1200.0, \"date\": \"2024-09-01\"}"))
            .andExpect(status().isCreated());

        Account updatedAccount = accountRepository.findByAccountNumber("478758").orElseThrow();
        assertEquals(new BigDecimal("1200.0"), updatedAccount.getBalance());
    }
}
