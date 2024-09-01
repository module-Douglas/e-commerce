package io.github.douglas.ms_inventory.dto;

import io.github.douglas.ms_inventory.enums.Status;

import java.util.UUID;

public record UpdateProductStatusDTO (
        UUID inventoryId,
        UUID productId,
        Status status
) {
}
