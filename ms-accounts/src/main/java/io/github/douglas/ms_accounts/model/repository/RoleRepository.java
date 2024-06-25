package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Boolean existsByName(String name);
}
