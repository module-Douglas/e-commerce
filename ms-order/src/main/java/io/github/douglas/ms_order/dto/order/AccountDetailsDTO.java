package io.github.douglas.ms_order.dto.order;

import java.util.UUID;

public record AccountDetailsDTO(
        UUID accountId,
        String email
) {
}
