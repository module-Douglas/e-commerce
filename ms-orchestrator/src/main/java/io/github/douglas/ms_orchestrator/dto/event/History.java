package io.github.douglas.ms_orchestrator.dto.event;


import io.github.douglas.ms_orchestrator.enums.Sources;
import io.github.douglas.ms_orchestrator.enums.Status;

import java.time.LocalDateTime;

public record History(
        Sources source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
}
