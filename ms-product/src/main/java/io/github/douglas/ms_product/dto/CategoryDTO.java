package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Category;

import java.util.UUID;

public record CategoryDTO(
        UUID id,
        String name
) {
    public CategoryDTO generateDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
