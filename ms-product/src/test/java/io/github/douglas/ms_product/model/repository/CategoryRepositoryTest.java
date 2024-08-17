package io.github.douglas.ms_product.model.repository;

import io.github.douglas.ms_product.model.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Exists Category by name true.")
    public void existsByNameTrueTest() {
        entityManager.persist(getValidCategory());

        assertThat(categoryRepository.existsByName(getValidCategory().getName()))
                .isTrue();
    }

    @Test
    @DisplayName("Exists Category by name false.")
    public void existsByNameFalseTest() {
        assertThat(categoryRepository.existsByName(getValidCategory().getName()))
                .isFalse();
    }

    @Test
    @DisplayName("Find Category by id success.")
    public void findCategoryByIdSuccessTest() {
        var category = getValidCategory();
        entityManager.persist(category);

        var response = categoryRepository.findById(category.getId());

        assertThat(response.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Find Category by id fail.")
    public void findCategoryByIdFailTest() {
        assertThat(categoryRepository.findById(UUID.randomUUID()))
                .isEmpty();
    }

    @Test
    @DisplayName("Save Category.")
    public void saveCategoryTest() {
        var response = categoryRepository.save(getValidCategory());

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(getValidCategory().getName());
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Delete Category.")
    public void deleteCategoryTest() {
        var category = getValidCategory();
        entityManager.persist(category);

        categoryRepository.delete(category);

        assertThat(categoryRepository.findById(category.getId()))
                .isEmpty();
    }


    private static Category getValidCategory() {
        return new Category(null, "CATEGORY");
    }




}
