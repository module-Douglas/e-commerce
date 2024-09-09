package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.dto.GenericIdHandler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    BrandDTO registerBrand(BrandDTO request);

    BrandDTO getBrandById(UUID request);

    List<BrandDTO> getAllBrands();

    BrandDTO updateBrand(BrandDTO request);

    void deleteBrand(GenericIdHandler request);
}
