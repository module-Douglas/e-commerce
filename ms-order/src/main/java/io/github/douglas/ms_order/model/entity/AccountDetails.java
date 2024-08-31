package io.github.douglas.ms_order.model.entity;

import java.util.UUID;

public class AccountDetails {

    private UUID accountId;
    private String email;

    public AccountDetails() {
    }

    public AccountDetails(UUID accountId) {
        this.accountId = accountId;
    }

    public AccountDetails(UUID accountId, String email) {
        this.accountId = accountId;
        this.email = email;
    }

    public UUID getUserId() {
        return accountId;
    }

    public void setUserId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
