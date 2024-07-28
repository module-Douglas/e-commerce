package io.github.douglas.ms_inventory.dto.event;

import java.util.UUID;

public record AccountDetails(
        UUID userId,
        String email
) {
}
