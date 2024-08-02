package io.github.douglas.ms_accounts.dto;

public record ResetPasswordDTO(
        String validationCode,
        String accountEmail,
        String password,
        String confirmPassword
) {
}
