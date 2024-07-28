package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.dto.*;
import io.github.douglas.ms_accounts.model.entity.Account;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.model.repository.AccountRepository;
import io.github.douglas.ms_accounts.service.TokenService;
import io.github.douglas.ms_accounts.service.AccountService;
import org.apache.kafka.common.errors.AuthenticationException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public AccountDTO register(RegisterAccountDTO request) {
        cpfCheck(request.cpf());
        emailCheck(request.email());
        request.roles().forEach(this::roleCheck);

        var roles = request.roles().stream()
                .map(role -> roleRepository.findById(role)
                        .orElseThrow())
                .collect(Collectors.toSet());
        var account = new Account(request, passwordEncoder.encode(request.password()));
        account.setRoles(roles);

        return new AccountDTO(accountRepository.save(account));
    }

    @Override
    public AccessTokenDTO login(LoginDTO loginRequest) {
        var account = accountRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with email: %s", loginRequest.email())));

        if (!validatePassword(loginRequest.password(), account)) {
            throw new AuthenticationException("Invalid password. Try again or reset you password.");
        }

        return tokenService.generateTokens(account);
    }

    @Override
    public AccountDTO getUserDetails(UUID id) {
        return new AccountDTO(accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with id: %s", id))));
    }

    @Override
    public void updatePassword(UpdatePasswordDTO request) {
        var account = accountRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with id: %s", request.userId())));
        account.setPassword(passwordEncoder.encode(request.newPassword()));
        accountRepository.save(account);
    }

    private void cpfCheck(String cpf) {
        if (accountRepository.existsByCpf(cpf)) throw new DataIntegrityViolationException("CPF already registered.");
    }

    private void emailCheck(String email) {
        if (accountRepository.existsByEmail(email)) throw new DataIntegrityViolationException("Email already in use. Try to reset your password");
    }

    private void roleCheck(UUID roleId) {
        if (!roleRepository.existsById(roleId)) throw new ResourceNotFoundException(format("Role not found with id: %s", roleId));
    }

    private Boolean validatePassword(String password, Account account) {
        return passwordEncoder.matches(password, account.getPassword());
    }

}
