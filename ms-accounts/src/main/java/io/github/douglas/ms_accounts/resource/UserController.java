package io.github.douglas.ms_accounts.resource;

import io.github.douglas.ms_accounts.dto.LoginDTO;
import io.github.douglas.ms_accounts.dto.RefreshTokenDTO;
import io.github.douglas.ms_accounts.dto.RegisterUserDTO;
import io.github.douglas.ms_accounts.service.TokenService;
import io.github.douglas.ms_accounts.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserDetails(id));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAccessToken(@RequestBody RefreshTokenDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenService.generateTokensByRefreshToken(request));
    }

}
