package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.dto.RoleDTO;
import io.github.douglas.ms_accounts.model.entity.Role;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.service.RoleService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

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

    @Override
    public void deleteRole(UUID id) {
        var role = roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(format("Role not found with id: %s", id)));
        roleRepository.delete(role);
    }

    private void roleCheck(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.name())) {
            throw new DataIntegrityViolationException(format("Role: %s already exists.", roleDTO.name()));
        }
    }
}
