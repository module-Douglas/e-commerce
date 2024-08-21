package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.model.entity.Brand;
import io.github.douglas.ms_product.model.entity.Product;
import io.github.douglas.ms_product.model.repository.BrandRepository;
import io.github.douglas.ms_product.service.impl.BrandServiceImpl;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BrandServiceTest {

    BrandService brandService;

    @MockBean
    BrandRepository brandRepository;

    @BeforeEach
    public void setUp() {
        this.brandService = new BrandServiceImpl(brandRepository);
    }

    @Test
    @DisplayName("Register Brand success.")
    public void registerBrandSuccessTest() {
        var brand = getValidBrand();
        var request = getValidBrandDTO();

        when(brandRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(brandRepository.save(any(Brand.class)))
                .thenReturn(brand);

        var receivedBrand = brandService.registerBrand(request);

        assertThat(receivedBrand.id()).isNotNull();
        assertThat(receivedBrand.id()).isEqualTo(brand.getId());
        assertThat(receivedBrand.name()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("Register Brand fail.")
    public void registerBrandFailTest() {
        var name = getValidBrand().getName();
        when(brandRepository.existsByName(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> brandService.registerBrand(getValidBrandDTO()));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Brand with name %s already registered.", name));
    }

    @Test
    @DisplayName("Get Brand by id success.")
    public void getBrandByIdSuccessTest() {
        var brand = getValidBrand();

        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(brand));

        var response = brandService.getBrandById(brand.getId());

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isEqualTo(brand.getId());
        assertThat(response.name()).isEqualTo(brand.getName());
    }

    @Test
    @DisplayName("Get brand by id fail.")
    public void getBrandByIdFailTest() {
        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> brandService.getBrandById(getValidBrand().getId()));

        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(format("Brand not found with id: %s.", getValidBrand().getId()));
    }

    @Test
    @DisplayName("Get all brands.")
    public void getAllBrandTest() {
        List<Brand> brands = new ArrayList<>();
        brands.add(getValidBrand());
        brands.add(getValidBrand());

        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(brands, pageable, brands.size());

        when(brandRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        var response = brandService.getAllBrands(pageable);

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(brands.size());
        assertThat(response.getContent().size()).isEqualTo(brands.size());
    }

    @Test
    @DisplayName("Get empty list of brands.")
    public void getEmptyListOfBrandsTest() {
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<Brand>(new ArrayList<>(), pageable, 0);

        when(brandRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        var response = brandService.getAllBrands(pageable);

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(0);
        assertThat(response.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Update Brand success.")
    public void UpdateBrandSuccessTest() {
        var brand = getValidBrand();
        brand.setName("UPDATED_BRAND");
        var request = getValidBrandDTOWithId();

        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class)))
                .thenReturn(brand);

        var response = brandService.updateBrand(request);

        assertThat(response.id()).isEqualTo(brand.getId());
        assertThat(response.name()).isEqualTo(brand.getName());
    }

    @Test
    @DisplayName("Update Brand fail cause name.")
    public void updateBrandFailCauseNameTest() {
        when(brandRepository.existsByName(any(String.class)))
                .thenReturn(true);

        var exception = catchThrowable(() -> brandService.updateBrand(getValidBrandDTO()));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Brand with name %s already registered.", getValidBrand().getName()));
    }

    @Test
    @DisplayName("Update Brand fail cause not found.")
    public void updateBrandFailCauseNotFoundTest() {
        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> brandService.updateBrand(getValidBrandDTOWithId()));

        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(format("Brand not found with id: %s.", getValidBrandDTOWithId().id()));
    }

    @Test
    @DisplayName("Delete Brand success.")
    public void deleteBrandSuccessTest() {
        var brand = getValidBrand();
        brand.setProduct(new HashSet<>());

        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(brand));

        brandService.deleteBrand(getValidBrandDTOWithId());

        verify(brandRepository, times(1)).delete(brand);
    }

    @Test
    @DisplayName("Delete Brand fail cause not found.")
    public void deleteBrandFailCauseNotFoundTest() {
        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var exception = catchThrowable(() -> brandService.deleteBrand(getValidBrandDTOWithId()));

        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(format("Brand not found with id: %s.", getValidBrandDTOWithId().id()));
        verify(brandRepository, never()).delete(any(Brand.class));
    }

    @Test
    @DisplayName("Delete Brand fail cause is in use.")
    public void deleteBrandFailCauseInUseTest() {
        var brand = getValidBrand();
        brand.setProduct(Set.of(new Product()));

        when(brandRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(brand));

        var exception = catchThrowable(() -> brandService.deleteBrand(getValidBrandDTOWithId()));

        assertThat(exception)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(format("Brand %s cannot be deleted as it is referenced by products.", brand.getId()));
        verify(brandRepository, never()).delete(any(Brand.class));
    }

    private static Brand getValidBrand() {
        return new Brand(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "BRAND");
    }

    private static BrandDTO getValidBrandDTO() {
        return new BrandDTO(null, "BRAND", null, null);
    }

    private static BrandDTO getValidBrandDTOWithId() {
        return new BrandDTO(UUID.fromString("f49955bb-7e93-44a0-a46f-e764a75dc199"), "UPDATED_BRAND", null, null);
    }

}
