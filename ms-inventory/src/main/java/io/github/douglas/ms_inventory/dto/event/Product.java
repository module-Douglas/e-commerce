package io.github.douglas.ms_inventory.dto.event;

import java.math.BigDecimal;
import java.util.UUID;

public record Product(
        UUID productId,
        BigDecimal unitValue,
        Long quantity
) {
}
