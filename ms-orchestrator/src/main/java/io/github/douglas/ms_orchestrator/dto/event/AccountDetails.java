package io.github.douglas.ms_orchestrator.dto.event;

import java.util.UUID;

public record AccountDetails(
        UUID userId,
        String email
) {
}
