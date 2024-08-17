package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.BrandDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    BrandDTO registerBrand(BrandDTO request);

    @Cacheable(value = "brand", key = "#id")
    BrandDTO getBrandById(UUID id);

    @Cacheable(value = "brands")
    List<BrandDTO> getAllBrands();

    BrandDTO updateBrand(BrandDTO request);

    void deleteBrand(BrandDTO request);
}
