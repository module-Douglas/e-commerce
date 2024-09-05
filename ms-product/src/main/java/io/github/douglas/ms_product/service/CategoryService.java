package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.dto.GenericIdHandler;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDTO registerCategory(CategoryDTO request);

    CategoryDTO getCategoryById(GenericIdHandler request);

    List<CategoryDTO> getAll();

    void deleteCategory(GenericIdHandler request);
}
