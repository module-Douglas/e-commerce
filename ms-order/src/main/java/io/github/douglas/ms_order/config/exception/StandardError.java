package io.github.douglas.ms_order.config.exception;

import java.time.LocalDateTime;

public record StandardError(
        String path,
        Integer statusCode,
        String message,
        LocalDateTime raisedAt
) {
}
