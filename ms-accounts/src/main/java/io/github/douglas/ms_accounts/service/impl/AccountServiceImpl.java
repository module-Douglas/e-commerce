package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.broker.KafkaProducer;
import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.*;
import io.github.douglas.ms_accounts.enums.Type;
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

import static io.github.douglas.ms_accounts.enums.Type.EMAIL;
import static io.github.douglas.ms_accounts.enums.Type.PASSWORD;
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
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(loginRequest.email())));

        if (!validatePassword(loginRequest.password(), account))
            throw new AuthenticationException("Invalid password. Try again or reset you password.");

        return tokenService.generateTokens(account);
    }

    @Override
    public AccountDTO getUserDetails(UUID id) {
        return new AccountDTO(accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithIdMessage(id))));
    }

    @Override
    public ResetCodeDTO requestResetPassword(UpdatePasswordRequestDTO request) {
        accountRepository.findByEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.accountEmail())));

        var resetCode = generateResetCode(request.accountEmail(), PASSWORD);

        var alreadyRegisteredCode = resetCodeRepository.findByAccountEmail(request.accountEmail());
        alreadyRegisteredCode.ifPresent(resetCodeRepository::delete);

        resetCodeRepository.save(resetCode);
        var message = format("Your password reset code is: %s", resetCode.getResetCode());
        kafkaProducer.sendMail(
                jsonUtil.toJson(new EmailRequestDTO("UPDATE PASSWORD CONFIRMATION CODE", resetCode.getAccountEmail(), message))
        );

        return new ResetCodeDTO(resetCode);
    }

    @Override
    public void resetPassword(UpdatePasswordDTO request) {
        var resetCode = resetCodeRepository.findByAccountEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Cannot found a reset password request for this email: %s", request.accountEmail())));

        validateResetCode(request.validationCode(), resetCode);
        validatePasswords(request);
        validateType(PASSWORD, resetCode.getType());

        var account = accountRepository.findByEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.accountEmail())));

        account.setPassword(passwordEncoder.encode(request.password()));
        accountRepository.save(account);
        resetCodeRepository.delete(resetCode);
    }

    @Override
    public ResetCodeDTO changeEmailRequest(ChangeEmailRequestDTO request) {
        var account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithIdMessage(request.accountId())));

        if (!account.getEmail().equals(request.currentEmail()))
            throw new ValidationException("Something went wrong. Current email doesn't match with account registered email.");
        emailCheck(request.newEmail());

        var resetCode = generateResetCode(request.currentEmail(), EMAIL);

        var alreadyRegisteredCode = resetCodeRepository.findByAccountEmail(account.getEmail());
        alreadyRegisteredCode.ifPresent(resetCodeRepository::delete);

        resetCodeRepository.save(resetCode);
        var message = format("Your change email confirmation code is: %s", resetCode.getResetCode());
        kafkaProducer.sendMail(
                jsonUtil.toJson(new EmailRequestDTO("UPDATE EMAIL CONFIRMATION CODE", resetCode.getAccountEmail(), message))
        );

        return new ResetCodeDTO(resetCode);
    }

    @Override
    public void changeEmail(ChangeEmailDTO request) {
        var resetCode = resetCodeRepository.findByAccountEmail(request.currentEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Cannot found a reset password request for this email: %s.", request.currentEmail())));

        validateResetCode(request.resetCode(), resetCode);
        emailCheck(request.newEmail());
        validateType(EMAIL, resetCode.getType());

        var account = accountRepository.findByEmail(request.currentEmail())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.currentEmail())));
        account.setEmail(request.newEmail());
        accountRepository.save(account);
        resetCodeRepository.delete(resetCode);
    }

    @Override
    public void banAccount(AccountDTO accountDTO) {
        accountRepository.deleteById(accountDTO.id());
    }

    @Override
    public AccountDTO updateAccountDetails(AccountDTO request) {
        var account = accountRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithIdMessage(request.id())));

        if (!account.getEmail().equals(request.email()))
            throw new ValidationException("Email must be changed at /change-email endpoint.");

        account.setFirstName(request.firstName());
        account.setLastName(request.lastName());
        account.setEmail(request.email());

        return new AccountDTO(
                accountRepository.save(account)
        );
    }

    private void cpfCheck(String cpf) {
        if (accountRepository.existsByCpf(cpf))
            throw new DataIntegrityViolationException("CPF already registered.");
    }

    private void emailCheck(String email) {
        if (accountRepository.existsByEmail(email))
            throw new DataIntegrityViolationException("Email already in use. Try to reset your password.");
    }

    private void roleCheck(UUID roleId) {
        if (!roleRepository.existsById(roleId))
            throw new ResourceNotFoundException(format("Role not found with id: %s.", roleId));
    }

    private Boolean validatePassword(String password, Account account) {
        return passwordEncoder.matches(password, account.getPassword());
    }

    private void validateResetCode(String request, ResetCode resetCode) {
        if (resetCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            resetCodeRepository.delete(resetCode);
            throw new ValidationException("Expired Reset Code. Please do a new reset password request.");
        }

        if (!request.equals(resetCode.getResetCode()))
            throw new ValidationException("Invalid Reset Code.");
    }

    private static void validatePasswords(UpdatePasswordDTO request) {
       if (!request.password().equals(request.confirmPassword()))
           throw new ValidationException("Passwords didn't matches.");
    }

    private static void validateType(Type expected, Type received) {
        if (!expected.equals(received))
            throw new ValidationException("Reset code Type doesn't match with requested resource.");
    }

    private static ResetCode generateResetCode(String accountEmail, Type type) {
        var code = 100_000 + (int)(Math.random() * ((999_999 - 100_000) + 1));
        return new ResetCode(
                valueOf(code),
                accountEmail,
                LocalDateTime.now().plusMinutes(3),
                type
        );
    }

    private static String accountNotFoundWithEmailMessage(String email) {
        return format("Account not found with email: %s.", email);
    }

    private static String accountNotFoundWithIdMessage(UUID id) {
        return format("Account not found with id: %s.", id);
    }
}
