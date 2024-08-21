package io.github.douglas.ms_accounts.dto;

import java.util.UUID;

public record ChangeEmailRequestDTO(
        UUID accountId,
        String currentEmail,
        String newEmail
) {
}
