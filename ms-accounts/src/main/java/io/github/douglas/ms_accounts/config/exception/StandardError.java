package io.github.douglas.ms_accounts.config.exception;

import java.time.LocalDateTime;

public record StandardError(
        String path,
        Integer statusCode,
        String message,
        LocalDateTime raisedAt
) {
}
