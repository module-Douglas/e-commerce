package io.github.douglas.ms_product.dto;

import java.util.UUID;

public record LinkInventory(
        UUID productId,
        UUID inventoryId
) {
}
