package io.github.douglas.ms_accounts.dto;

public record ChangeEmailDTO(
        String resetCode,
        String currentEmail,
        String newEmail
) {
}
