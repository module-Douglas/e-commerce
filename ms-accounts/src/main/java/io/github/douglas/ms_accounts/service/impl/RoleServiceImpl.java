package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.dto.RoleDTO;
import io.github.douglas.ms_accounts.model.entity.Role;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.service.RoleService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO registerRole(RoleDTO request) {
        roleCheck(request);
        return new RoleDTO(roleRepository.save(new Role(request)));
    }

    private void roleCheck(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.name())) {
            throw new DataIntegrityViolationException(String.format("Role: %s already exists.", roleDTO.name()));
        }
    }
}
