package io.github.douglas.ms_accounts.model.entity;

import io.github.douglas.ms_accounts.enums.Type;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_password_reset_codes")
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String confirmationCode;
    @Column(unique = true)
    private String accountEmail;
    private LocalDateTime expiresAt;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public ConfirmationCode() {

    }

    public ConfirmationCode(String confirmationCode, String accountEmail, LocalDateTime expiresAt, Type type) {
        this.confirmationCode = confirmationCode;
        this.accountEmail = accountEmail;
        this.expiresAt = expiresAt;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResetCode() {
        return confirmationCode;
    }

    public void setResetCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
