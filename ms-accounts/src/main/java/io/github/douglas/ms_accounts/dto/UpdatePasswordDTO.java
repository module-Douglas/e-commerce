package io.github.douglas.ms_accounts.dto;

import java.util.UUID;

public record UpdatePasswordDTO(
        UUID userId,
        String newPassword
) {
}
