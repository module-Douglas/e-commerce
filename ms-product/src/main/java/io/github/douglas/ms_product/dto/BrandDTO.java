package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Brand;

import java.time.LocalDateTime;
import java.util.UUID;

public record BrandDTO(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public BrandDTO(Brand brand) {
        this(
                brand.getId(),
                brand.getName(),
                brand.getCreatedAt(),
                brand.getUpdatedAt()
        );
    }
}
