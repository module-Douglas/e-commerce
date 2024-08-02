package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetCodeRepository extends JpaRepository<ResetCode, UUID> {

    Optional<ResetCode> findByAccountEmail(String accountEmail);
}
