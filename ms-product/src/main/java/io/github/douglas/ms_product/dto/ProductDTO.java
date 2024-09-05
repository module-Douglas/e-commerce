package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.enums.Status;
import io.github.douglas.ms_product.model.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal unitValue,
        String brand,
        UUID inventoryId,
        Status status,
        UUID supplierId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<CategoryDTO> categories
) implements Serializable {

    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getUnitValue(),
                product.getBrand().getName(),
                product.getInventoryId(),
                product.getStatus(),
                product.getSupplier().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategories().stream()
                        .map(CategoryDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}
