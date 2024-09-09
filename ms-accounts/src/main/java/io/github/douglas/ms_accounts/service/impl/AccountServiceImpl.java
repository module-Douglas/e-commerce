package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.broker.KafkaProducer;
import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.*;
import io.github.douglas.ms_accounts.enums.Type;
import io.github.douglas.ms_accounts.model.entity.Account;
import io.github.douglas.ms_accounts.model.entity.ConfirmationCode;
import io.github.douglas.ms_accounts.model.repository.ConfirmationCodeRepository;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.douglas.ms_accounts.enums.Type.EMAIL;
import static io.github.douglas.ms_accounts.enums.Type.PASSWORD;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Integer CPF_LENGTH = 11;
    private static final Integer FIRST_DIGIT_INDEX = 9;
    private static final Integer SECOND_DIGIT_INDEX = 10;
    private static final Integer FIRST_DIGIT_MULTIPLIER = 10;
    private static final Integer SECOND_DIGIT_MULTIPLIER = 11;
    private static final String CPF_PATTERN = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    private final AccountRepository accountRepository;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            ConfirmationCodeRepository confirmationCodeRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            KafkaProducer kafkaProducer,
            JsonUtil jsonUtil
    ) {
        this.accountRepository = accountRepository;
        this.confirmationCodeRepository = confirmationCodeRepository;
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
        var account = new Account(request, passwordEncoder.encode(request.password()), roles);

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
    public ResetCodeDTO requestResetPassword(GenericEmailHandler request) {
        accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.email())));

        var resetCode = generateResetCode(request.email(), PASSWORD);

        var alreadyRegisteredCode = confirmationCodeRepository.findByAccountEmail(request.email());
        alreadyRegisteredCode.ifPresent(confirmationCodeRepository::delete);

        confirmationCodeRepository.save(resetCode);
        var message = format("Your password reset code is: %s", resetCode.getResetCode());
        kafkaProducer.sendMail(
                jsonUtil.toJson(new EmailRequestDTO("UPDATE PASSWORD CONFIRMATION CODE", resetCode.getAccountEmail(), message))
        );

        return new ResetCodeDTO(resetCode);
    }

    @Override
    public void resetPassword(UpdatePasswordDTO request) {
        var resetCode = confirmationCodeRepository.findByAccountEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Cannot found a reset password request for this email: %s", request.accountEmail())));

        validateResetCode(request.validationCode(), resetCode);
        validatePasswords(request);
        validateType(PASSWORD, resetCode.getType());

        var account = accountRepository.findByEmail(request.accountEmail())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.accountEmail())));

        account.setPassword(passwordEncoder.encode(request.password()));
        accountRepository.save(account);
        confirmationCodeRepository.delete(resetCode);
    }

    @Override
    public ResetCodeDTO changeEmailRequest(GenericEmailHandler request) {
        var account = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.email())));

        if (!account.getEmail().equals(request.email()))
            throw new ValidationException("Something went wrong. Current email doesn't match with account registered email.");

        var resetCode = generateResetCode(request.email(), EMAIL);

        var alreadyRegisteredCode = confirmationCodeRepository.findByAccountEmail(account.getEmail());
        alreadyRegisteredCode.ifPresent(confirmationCodeRepository::delete);

        confirmationCodeRepository.save(resetCode);
        var message = format("Your change email confirmation code is: %s", resetCode.getResetCode());
        kafkaProducer.sendMail(
                jsonUtil.toJson(new EmailRequestDTO("UPDATE EMAIL CONFIRMATION CODE", resetCode.getAccountEmail(), message))
        );

        return new ResetCodeDTO(resetCode);
    }

    @Override
    public void changeEmail(ChangeEmailDTO request) {
        var resetCode = confirmationCodeRepository.findByAccountEmail(request.currentEmail())
                .orElseThrow(() -> new ResourceNotFoundException(format("Cannot found a reset password request for this email: %s.", request.currentEmail())));

        validateResetCode(request.validationCode(), resetCode);
        emailCheck(request.newEmail());
        validateType(EMAIL, resetCode.getType());

        var account = accountRepository.findByEmail(request.currentEmail())
                .orElseThrow(() -> new ResourceNotFoundException(accountNotFoundWithEmailMessage(request.currentEmail())));
        account.setEmail(request.newEmail());
        accountRepository.save(account);
        confirmationCodeRepository.delete(resetCode);
    }

    @Override
    public void deleteAccount(GenericIdHandler request) {
        accountRepository.deleteById(request.id());
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
                accountRepository.save(account));
    }

    private void cpfCheck(String cpf) {
        if (!Pattern.matches(CPF_PATTERN, cpf))
            throw new ValidationException("Invalid CPF format.");

        var formatedCpf = cleanCpf(cpf);
        if (formatedCpf.length() != CPF_LENGTH)
            throw new ValidationException("Invalid CPF size.");

        var sum = 0;
        for (int i = 0; i < FIRST_DIGIT_INDEX; i++) {
            sum += parseInt(valueOf(formatedCpf.charAt(i))) * (FIRST_DIGIT_MULTIPLIER - i);
        }

        var mod = (sum % 11);
        char firstDigit = (mod == 0 || mod == 1) ? '0' : (char) ('0' + (11 - mod));

        sum = 0;

        for (int i = 0; i < SECOND_DIGIT_INDEX; i++) {
            sum += parseInt(valueOf(formatedCpf.charAt(i))) * (SECOND_DIGIT_MULTIPLIER - i);
        }

        mod = (sum % 11);
        char secondDigit = (mod == 0 || mod == 1) ? '0' : (char) ('0' + (11 - mod));

        if ((formatedCpf.charAt(FIRST_DIGIT_INDEX) != firstDigit) || (formatedCpf.charAt(SECOND_DIGIT_INDEX) != secondDigit))
            throw new ValidationException("Invalid CPF.");

        if (accountRepository.existsByCpf(formatedCpf))
            throw new DataIntegrityViolationException(format("CPF %s already registered.", cpf));
    }

    private void emailCheck(String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email))
            throw new ValidationException("Invalid email.");

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

    private void validateResetCode(String request, ConfirmationCode confirmationCode) {
        if (confirmationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            confirmationCodeRepository.delete(confirmationCode);
            throw new ValidationException("Expired Reset Code. Please do a new reset password request.");
        }

        if (!request.equals(confirmationCode.getResetCode()))
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

    private static ConfirmationCode generateResetCode(String accountEmail, Type type) {
        var code = 100_000 + (int)(Math.random() * ((999_999 - 100_000) + 1));
        return new ConfirmationCode(
                valueOf(code),
                accountEmail,
                LocalDateTime.now().plusMinutes(3),
                type
        );
    }

    private String cleanCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private static String accountNotFoundWithEmailMessage(String email) {
        return format("Account not found with email: %s.", email);
    }

    private static String accountNotFoundWithIdMessage(UUID id) {
        return format("Account not found with id: %s.", id);
    }
}
