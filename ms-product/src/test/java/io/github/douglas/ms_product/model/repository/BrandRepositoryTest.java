package io.github.douglas.ms_product.model.repository;

import io.github.douglas.ms_product.model.entity.Brand;
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
class BrandRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("Exists Brand by name true.")
    public void existsByNameTrueTest() {
        entityManager.persist(getValidBrand());

        assertThat(brandRepository.existsByName(getValidBrand().getName()))
                .isTrue();
    }
    @Test
    @DisplayName("Exists Brand by name false.")
    public void existsByNameFalseTest() {
        assertThat(brandRepository.existsByName(getValidBrand().getName()))
                .isFalse();
    }

    @Test
    @DisplayName("Find Brand by id success.")
    public void findByIdSuccessTest() {
        var brand = getValidBrand();
        entityManager.persist(brand);

        var response = brandRepository.findById(brand.getId());

        assertThat(response.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Find Brand by id fail.")
    public void findByIdFailTest() {
        assertThat(brandRepository.findById(UUID.randomUUID()))
                .isEmpty();
    }

    @Test
    @DisplayName("Save Brand.")
    public void saveBrandTest() {
        var response = brandRepository.save(getValidBrand());

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(getValidBrand().getName());
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Delete Brand.")
    public void deleteBrandTest() {
        var brand = getValidBrand();
        entityManager.persist(brand);

        brandRepository.delete(brand);

        assertThat(brandRepository.findById(brand.getId()))
                .isEmpty();
    }

    private static Brand getValidBrand() {
        return new Brand(null, "BRAND");
    }

}
