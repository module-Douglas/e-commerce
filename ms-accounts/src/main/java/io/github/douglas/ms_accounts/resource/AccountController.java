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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.getUserDetails(id));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAccessToken(@RequestBody RefreshTokenDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenService.generateTokensByRefreshToken(request));
    }

    @PostMapping("/password-update")
    public ResponseEntity<?> requestPasswordUpdated(@RequestBody UpdatePasswordRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.requestResetPassword(request));
    }

    @PatchMapping("/password-update")
    public ResponseEntity<?> passwordUpdate(@RequestBody UpdatePasswordDTO request) {
        accountService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> banAccount(@RequestBody AccountDTO request) {
        accountService.banAccount(request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }



}
