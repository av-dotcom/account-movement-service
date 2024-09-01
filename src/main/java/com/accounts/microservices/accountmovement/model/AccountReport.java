package com.accounts.microservices.accountmovement.model;

import java.util.List;

public class AccountReport {
    private Account account;
    private List<Movement> movements;

    public AccountReport(Account account, List<Movement> movements) {
        this.account = account;
        this.movements = movements;
    }
}
