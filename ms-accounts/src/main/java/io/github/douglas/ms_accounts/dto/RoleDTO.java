package io.github.douglas.ms_accounts.dto;

import io.github.douglas.ms_accounts.model.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleDTO(
        UUID id,
        String name,
        LocalDateTime createdAt
) {
    public RoleDTO(Role role) {
        this(
                role.getId(),
                role.getName(),
                role.getCreatedAt()
        );
    }
}
