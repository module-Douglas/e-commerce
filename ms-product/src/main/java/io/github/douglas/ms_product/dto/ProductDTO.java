package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductDTO(
        String id,
        String name,
        String description,
        String brand,
        String inventoryId,
        String supplierId,
        Set<UUID> categories
) {

    public ProductDTO(Product product) {
        this(
                product.getId().toString(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getInventoryId(),
                product.getSupplier().getId().toString(),
                product.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())
        );
    }
}
