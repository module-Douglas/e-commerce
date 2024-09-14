package io.github.douglas.ms_inventory.dto;

import java.util.UUID;

public record UpdateStockAmountDTO(
        UUID inventoryId,
        Long value
) {
}
