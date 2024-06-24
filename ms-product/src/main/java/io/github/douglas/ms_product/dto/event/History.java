package io.github.douglas.ms_product.dto.event;

import io.github.douglas.ms_product.enums.Sources;
import io.github.douglas.ms_product.enums.Status;

import java.time.LocalDateTime;

public record History(
        Sources source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
}
