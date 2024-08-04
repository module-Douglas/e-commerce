package io.github.douglas.ms_accounts.dto;

public record UpdatePasswordDTO(
        String validationCode,
        String accountEmail,
        String password,
        String confirmPassword
) {
}
