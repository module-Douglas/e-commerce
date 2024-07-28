package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.RefreshToken;
import io.github.douglas.ms_accounts.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUser(Account user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

}
