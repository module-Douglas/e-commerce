package io.github.douglas.ms_order.model.entity;

public class AccountDetails {

    private String userId;
    private String email;

    public AccountDetails() {
    }

    public AccountDetails(String userId) {
        this.userId = userId;
    }

    public AccountDetails(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
