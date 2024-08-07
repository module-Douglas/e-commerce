package io.github.douglas.ms_accounts.model.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String refreshToken;
    private Instant expiryAt;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account user;

    @PrePersist
    private void prePersist() {
        this.expiryAt = Instant.now().plusMillis(600000);
    }

    public RefreshToken() {

    }

    public RefreshToken(String refreshToken, Account user) {
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public RefreshToken(UUID id, String refreshToken, Account user) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public Instant getExpiryAt() {
        return expiryAt;
    }
}
