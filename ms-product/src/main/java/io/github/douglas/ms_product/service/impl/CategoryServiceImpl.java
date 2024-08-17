package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.service.CategoryService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO registerCategory(CategoryDTO request) {
        checkName(request.name().toUpperCase());
        return new CategoryDTO(
                categoryRepository.save(new Category(request.name().toUpperCase())));
    }

    @Cacheable(value = "category", key = "#id")
    @Override
    public CategoryDTO getCategoryById(UUID id) {
        return new CategoryDTO(
                categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(format("Category not found with id: %s", id))));
    }

    @Cacheable(value = "categories")
    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();
    }

    @Override
    public void deleteCategory(CategoryDTO request) {
        var category = categoryRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(format("Category not found with id: %s", request.id())));
        if(!category.getProducts().isEmpty())
            throw new DataIntegrityViolationException(format("Category %s cannot be deleted as it is referenced by products.", request.id()));

        categoryRepository.delete(category);
    }

    private void checkName(String name) {
        if (categoryRepository.existsByName(name))
            throw new DataIntegrityViolationException(format("Name %s already registered.", name));
    }
}
