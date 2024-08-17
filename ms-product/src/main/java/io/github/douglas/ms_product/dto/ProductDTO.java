package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductDTO(
        UUID id,
        String name,
        String description,
        UUID brandId,
        UUID inventoryId,
        UUID supplierId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<UUID> categories
) implements Serializable {

    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand().getId(),
                product.getInventoryId(),
                product.getSupplier().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())
        );
    }
}
