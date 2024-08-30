package io.github.douglas.ms_accounts.resource;

import io.github.douglas.ms_accounts.dto.*;
import io.github.douglas.ms_accounts.service.TokenService;
import io.github.douglas.ms_accounts.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TokenService tokenService;

    public AccountController(AccountService accountService, TokenService tokenService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterAccountDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.login(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.getUserDetails(id));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAccessToken(@RequestBody String request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenService.generateTokensByRefreshToken(request));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody String accountEmail) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.requestResetPassword(accountEmail));
    }

    @PatchMapping("/password-reset")
    public ResponseEntity<?> passwordReset(@RequestBody UpdatePasswordDTO request) {
        accountService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PatchMapping
    public ResponseEntity<?> updateAccountDetails(@RequestBody AccountDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.updateAccountDetails(request));
    }

    @DeleteMapping
    public ResponseEntity<?> banAccount(@RequestBody AccountDTO request) {
        accountService.banAccount(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> requestEmailUpdate(@RequestBody String accountEmail) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.changeEmailRequest(accountEmail));
    }

    @PatchMapping("/change-email")
    public ResponseEntity<?> emailUpdate(@RequestBody ChangeEmailDTO request) {
        accountService.changeEmail(request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}
