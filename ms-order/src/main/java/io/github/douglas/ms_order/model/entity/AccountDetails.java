package io.github.douglas.ms_order.model.entity;

import java.util.UUID;

public class AccountDetails {

    private UUID userId;
    private String email;

    public AccountDetails() {
    }

    public AccountDetails(UUID userId) {
        this.userId = userId;
    }

    public AccountDetails(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
