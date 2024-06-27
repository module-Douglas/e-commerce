package io.github.douglas.ms_accounts.dto;

import java.util.Set;

public record RegisterUserDTO(
        String firstName,
        String lastName,
        String email,
        String cpf,
        String password,
        Set<Long> roles
) {
}
