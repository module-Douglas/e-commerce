package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.BrandDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    BrandDTO registerBrand(BrandDTO request);

    BrandDTO getBrandById(UUID id);

    PageImpl<BrandDTO> getAllBrands(Pageable pageRequest);

    BrandDTO updateBrand(BrandDTO request);

    void deleteBrand(BrandDTO request);
}
