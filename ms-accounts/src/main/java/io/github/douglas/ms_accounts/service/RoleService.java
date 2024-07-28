package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.RoleDTO;

import java.util.UUID;

public interface RoleService {
    RoleDTO registerRole(RoleDTO roleDTO);

    void deleteRole(UUID id);
}
