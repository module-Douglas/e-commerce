package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.service.impl.CategoryServiceImpl;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class CategoryServiceTest {

    CategoryService categoryService;

    @MockBean
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        this.categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    @DisplayName("Register Category success.")
    public void registerCategorySuccessTest() {
        var category = getValidCategory();
        var request = getValidCategoryDTO();

        when(categoryRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        var receivedCategory = categoryService.registerCategory(request);

        assertThat(receivedCategory.id()).isEqualTo(category.getId());
        assertThat(receivedCategory.name()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("Register Category fail.")
    public void registerCategoryFailTest() {
        var request = getValidCategoryDTO();

        when(categoryRepository.existsByName(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> categoryService.registerCategory(request));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Name CATEGORY already registered.");

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Get Category by id success.")
    public void getCategoryByIdSuccessTest() {
        var category = getValidCategory();

        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(category));

        CategoryDTO receivedCategory = categoryService.getCategoryById(category.getId());

        assertThat(receivedCategory.id()).isEqualTo(category.getId());
        assertThat(receivedCategory.name()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("Get Category by id fail.")
    public void getCategoryByIdFailTest() {
        var id = getValidCategory().getId();

        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> categoryService.getCategoryById(id));

        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(format("Category not found with id: %s", id));
    }

    @Test
    @DisplayName("Get all categories.")
    public void getAllCategoriesTest() {
        List<Category> categories = new ArrayList<>();
        categories.add(getValidCategory());
        categories.add(getValidCategory());

        when(categoryRepository.findAll())
                .thenReturn(categories);

        var receivedCategories = categoryService.getAll();

        assertThat(receivedCategories).isNotNull();
        assertThat(receivedCategories.size()).isEqualTo(categories.size());
    }

    @Test
    @DisplayName("Get empty list of Categories")
    public void getEmptyAllCategoriesTest() {
        when(categoryRepository.findAll())
                .thenReturn(new ArrayList<>());

        var receivedCategories = categoryService.getAll();

        assertThat(receivedCategories).isEmpty();
    }

    @Test
    @DisplayName("Delete category success.")
    public void deleteCategorySuccessTest() {
        var category = getValidCategory();

        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(category));

        categoryService.deleteCategory(getValidCategoryDTO());

        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    @DisplayName("Delete category fail.")
    public void deleteCategoryFailTest() {
        when(categoryRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> categoryService.deleteCategory(getValidCategoryDTO()));

        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
        assertThat(exception).hasMessage(format("Category not found with id: %s", getValidCategoryDTO().id()));
        verify(categoryRepository, never()).delete(any(Category.class));
    }


    private static Category getValidCategory() {
        return new Category(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "CATEGORY");
    }

    private static CategoryDTO getValidCategoryDTO() {
        return new CategoryDTO(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "CATEGORY", null);
    }
}
