package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDTO registerCategory(CategoryDTO request);

    CategoryDTO getCategoryById(UUID id);

    List<CategoryDTO> getAll();

    void deleteCategory(CategoryDTO request);
}
