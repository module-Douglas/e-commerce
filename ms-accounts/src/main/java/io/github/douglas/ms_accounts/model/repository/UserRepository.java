package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
