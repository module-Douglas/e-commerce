package io.github.douglas.ms_accounts.dto;

import io.github.douglas.ms_accounts.model.entity.Role;

import java.time.LocalDateTime;

public record RoleDTO(
        Long id,
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
