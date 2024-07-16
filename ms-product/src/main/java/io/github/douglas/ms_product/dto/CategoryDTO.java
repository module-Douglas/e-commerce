package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Category;

public record CategoryDTO(
        Long id,
        String name
) {
    public CategoryDTO generateDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
