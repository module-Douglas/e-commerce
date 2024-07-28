package io.github.douglas.ms_inventory.dto;

import java.util.UUID;

public record LinkInventory(
        UUID productId,
        UUID inventoryId
) {
}
