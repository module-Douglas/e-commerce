package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO registerCategory(CategoryDTO request) {
        return request.generateDTO(
                categoryRepository.save(new Category(request)));
    }
}
