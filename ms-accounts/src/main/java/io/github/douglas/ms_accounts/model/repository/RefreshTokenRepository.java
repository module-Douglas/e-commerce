package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}
