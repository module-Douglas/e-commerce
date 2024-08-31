package io.github.douglas.ms_product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateInventoryDTO(
        UUID inventoryId,
        UUID productId,
        BigDecimal unitValue
) {
}
