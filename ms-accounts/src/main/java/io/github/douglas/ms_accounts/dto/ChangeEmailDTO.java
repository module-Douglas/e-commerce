package io.github.douglas.ms_accounts.dto;

public record ChangeEmailDTO(
        String validationCode,
        String currentEmail,
        String newEmail
) {
}
