package io.github.douglas.ms_accounts.dto;

import io.github.douglas.ms_accounts.model.entity.ResetCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResetCodeDTO(
        UUID id,
        String resetCode,
        String accountEmail,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
    public ResetCodeDTO(ResetCode resetCode) {
        this(
          resetCode.getId(),
          resetCode.getResetCode(),
          resetCode.getAccountEmail(),
          resetCode.getExpiresAt(),
          resetCode.getCreatedAt()
        );
    }
}
