package io.github.douglas.ms_accounts.dto;

import io.github.douglas.ms_accounts.model.entity.ConfirmationCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResetCodeDTO(
        UUID id,
        String resetCode,
        String accountEmail,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
    public ResetCodeDTO(ConfirmationCode confirmationCode) {
        this(
          confirmationCode.getId(),
          confirmationCode.getResetCode(),
          confirmationCode.getAccountEmail(),
          confirmationCode.getExpiresAt(),
          confirmationCode.getCreatedAt()
        );
    }
}
