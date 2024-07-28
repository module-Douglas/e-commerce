package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDTO(
        UUID id,
        String name,
        LocalDateTime createdAt
) {
    public CategoryDTO(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getCreatedAt()
        );
    }
}

