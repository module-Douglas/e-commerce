package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.broker.KafkaProducer;
import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.*;
import io.github.douglas.ms_accounts.model.entity.Account;
import io.github.douglas.ms_accounts.model.entity.ResetCode;
import io.github.douglas.ms_accounts.model.repository.ResetCodeRepository;
import io.github.douglas.ms_accounts.model.repository.RoleRepository;
import io.github.douglas.ms_accounts.model.repository.AccountRepository;
import io.github.douglas.ms_accounts.service.TokenService;
import io.github.douglas.ms_accounts.service.AccountService;
import io.github.douglas.ms_accounts.utils.JsonUtil;
import org.apache.kafka.common.errors.AuthenticationException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.String.valueOf;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ResetCodeRepository resetCodeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            ResetCodeRepository resetCodeRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            KafkaProducer kafkaProducer,
            JsonUtil jsonUtil
    ) {
        this.accountRepository = accountRepository;
        this.resetCodeRepository = resetCodeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.kafkaProducer = kafkaProducer;
        this.jsonUtil = jsonUtil;
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
    public ResetCodeDTO requestResetPassword(ResetPasswordRequestDTO request) {
        var code = 100_000 + (int)(Math.random() * ((999_999 - 100_000) + 1));
        var resetCode = new ResetCode(
                valueOf(code),
                request.accountEmail(),
                LocalDateTime.now().plusMinutes(3)
        );

        accountRepository.findByEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with email: %s", request.accountEmail())));

        var alreadyRegisteredCode = resetCodeRepository.findByAccountEmail(request.accountEmail());
        alreadyRegisteredCode.ifPresent(resetCodeRepository::delete);

        resetCodeRepository.save(resetCode);
        var message = format("Your password reset code is: %s", code);
        kafkaProducer.sendMail(
                jsonUtil.toJson(new EmailRequestDTO(resetCode.getAccountEmail(), message))
        );

        return new ResetCodeDTO(resetCode);
    }

    @Override
    public void resetPassword(ResetPasswordDTO request) {
        var resetCode = resetCodeRepository.findByAccountEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Cannot found a reset password request for this email: %s", request.accountEmail())));

        validateResetCode(request, resetCode);
        validatePasswords(request);

        var account = accountRepository.findByEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Account not found with email: %s", request.accountEmail())));

        account.setPassword(passwordEncoder.encode(request.password()));
        resetCodeRepository.delete(resetCode);
    }

    @Override
    public void banAccount(AccountDTO accountDTO) {
        accountRepository.deleteById(accountDTO.id());
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

    private void validateResetCode(ResetPasswordDTO request, ResetCode resetCode) {
        if (resetCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            resetCodeRepository.delete(resetCode);
            throw new ValidationException("Expired Reset Code. Please do a new reset password request");
        }

        if (!request.validationCode().equals(resetCode.getResetCode())) {
            throw new ValidationException("Invalid Reset Code.");
        }
    }

    private void validatePasswords(ResetPasswordDTO request) {
       if (!request.password().equals(request.confirmPassword())) throw new ValidationException("Passwords didn't matches.");
    }

}
