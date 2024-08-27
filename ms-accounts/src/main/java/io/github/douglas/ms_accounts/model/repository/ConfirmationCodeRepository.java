package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, UUID> {

    Optional<ConfirmationCode> findByAccountEmail(String accountEmail);
}
