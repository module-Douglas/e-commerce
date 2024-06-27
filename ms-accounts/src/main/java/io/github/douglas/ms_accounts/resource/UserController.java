package io.github.douglas.ms_accounts.resource;

import io.github.douglas.ms_accounts.dto.RegisterUserDTO;
import io.github.douglas.ms_accounts.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserDetails(id));
    }

}
