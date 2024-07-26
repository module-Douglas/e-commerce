package io.github.douglas.ms_accounts.service.impl;

import com.ctc.wstx.shaded.msv_core.util.StringPair;
import io.github.douglas.ms_accounts.dto.AccessTokenDTO;
import io.github.douglas.ms_accounts.dto.LoginDTO;
import io.github.douglas.ms_accounts.dto.RegisterUserDTO;
import io.github.douglas.ms_accounts.dto.UserDTO;
import io.github.douglas.ms_accounts.model.entity.User;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.model.repository.UserRepository;
import io.github.douglas.ms_accounts.service.TokenService;
import io.github.douglas.ms_accounts.service.UserService;
import org.apache.kafka.common.errors.AuthenticationException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
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
        var user = new User(request, passwordEncoder.encode(request.password()));
        user.setRoles(roles);

        return new UserDTO(userRepository.save(user));
    }

    @Override
    public AccessTokenDTO login(LoginDTO loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with email: %s", loginRequest.email())));

        if (!validatePassword(loginRequest.password(), user)) {
            throw new AuthenticationException("Invalid password. Try again or reset you password.");
        }

        return tokenService.generateTokens(user);
    }

    @Override
    public UserDTO getUserDetails(String id) {
        return new UserDTO(userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with id: %s", id))));
    }

    private void cpfCheck(String cpf) {
        if (userRepository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
    }

    private void emailCheck(String email) {
        if (userRepository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use. Try to reset your password");
    }

    private void roleCheck(UUID roleId) {
        if (!roleRepository.existsById(roleId)) throw new ResourceNotFoundException(format("Role not found with id: %s", roleId));
    }

    private Boolean validatePassword(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

}
