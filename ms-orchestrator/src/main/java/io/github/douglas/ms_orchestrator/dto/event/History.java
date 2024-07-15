package io.github.douglas.ms_orchestrator.dto.event;


import io.github.douglas.ms_orchestrator.enums.EventSource;
import io.github.douglas.ms_orchestrator.enums.Status;

import java.time.LocalDateTime;

public record History(
        EventSource source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
}
