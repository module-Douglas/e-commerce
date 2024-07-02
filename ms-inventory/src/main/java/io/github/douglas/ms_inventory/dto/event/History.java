package io.github.douglas.ms_inventory.dto.event;


import io.github.douglas.ms_inventory.enums.Sources;
import io.github.douglas.ms_inventory.enums.Status;

import java.time.LocalDateTime;

public record History(
        Sources source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
}
