package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.dto.RegisterUserDTO;
import io.github.douglas.ms_accounts.dto.RoleDTO;
import io.github.douglas.ms_accounts.dto.UserDTO;
import io.github.douglas.ms_accounts.model.entity.User;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.model.repository.UserRepository;
import io.github.douglas.ms_accounts.service.UserService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
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
    public UserDTO register(RegisterUserDTO request) {
        cpfCheck(request.cpf());
        emailCheck(request.email());
        request.roles().forEach(this::roleCheck);

        var roles = request.roles().stream()
                .map(role -> roleRepository.findById(role)
                        .orElseThrow())
                .collect(Collectors.toSet());
        var user = new User(request);
        user.setRoles(roles);

        return new UserDTO(userRepository.save(user));
    }

    private void cpfCheck(String cpf) {
        if (userRepository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
    }

    private void emailCheck(String email) {
        if (userRepository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use. Try to reset your password");
    }

    private void roleCheck(Long roleId) {
        if (!roleRepository.existsById(roleId)) throw new ResourceNotFoundException(String
                .format("Role not found with id: %s", roleId));
    }

}
