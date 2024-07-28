package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);

}
