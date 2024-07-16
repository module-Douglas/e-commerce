package io.github.douglas.ms_product.dto;


import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductDTO(
        String id,
        String name,
        String description,
        BigDecimal unitValue,
        Long stockAmount,
        String supplierId,
        Set<Long> categories
) {
    public ProductDTO generateDTO(Product product) {
        return new ProductDTO(
                product.getId().toString(),
                product.getName(),
                product.getDescription(),
                unitValue(),
                stockAmount(),
                product.getSupplier().getId().toString(),
                product.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())
        );
    }
}
