package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.enums.Status;

import java.util.UUID;

public record UpdateProductStatusDTO (
        UUID inventoryId,
        UUID productId,
        Status status
) {
}
