package io.github.douglas.ms_product.dto;


import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductDTO(
        String id,
        String name,
        String description,
        String brand,
        String inventoryId,
        BigDecimal unitValue,
        Long stockAmount,
        String supplierId,
        Set<UUID> categories
) {
    public ProductDTO generateDTO(Product product) {
        return new ProductDTO(
                product.getId().toString(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getInventoryId(),
                unitValue(),
                stockAmount(),
                product.getSupplier().getId().toString(),
                product.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())
        );
    }
}
