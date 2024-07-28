package io.github.douglas.ms_product.dto.event;

import java.util.UUID;

public record AccountDetails(
        UUID userId,
        String email
) {
}
