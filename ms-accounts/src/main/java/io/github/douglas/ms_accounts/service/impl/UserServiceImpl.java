package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.dto.RoleDTO;
import io.github.douglas.ms_accounts.dto.UserDTO;
import io.github.douglas.ms_accounts.model.entity.Role;
import io.github.douglas.ms_accounts.model.entity.User;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.model.repository.UserRepository;
import io.github.douglas.ms_accounts.service.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO register(UserDTO request) {
        cpfCheck(request.cpf());
        emailCheck(request.email());
        request.roles().forEach(this::roleCheck);
        var user = new User(request);
        user.setRoles(new HashSet<>());

        return new UserDTO(userRepository.save(user));
    }

    private void cpfCheck(String cpf) {
        if (userRepository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
    }

    private void emailCheck(String email) {
        if (userRepository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use. Try to reset your password");
    }

    private void roleCheck(RoleDTO role) {
        if (!roleRepository.existsById(role.id())) throw new ResourceNotFoundException(String
                .format("Role not found with id: %s", role.id()));
    }

}
