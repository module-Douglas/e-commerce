package io.github.douglas.ms_product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateInventoryDTO(
        UUID productId,
        BigDecimal unitValue
) {
}
