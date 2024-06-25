package io.github.douglas.ms_inventory.config.exception;

import java.time.LocalDateTime;

public record StandardError(
        String path,
        Integer statusCode,
        String message,
        LocalDateTime raisedAt
) {
}
