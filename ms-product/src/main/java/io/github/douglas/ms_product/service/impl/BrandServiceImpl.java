package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.dto.GenericIdHandler;
import io.github.douglas.ms_product.model.entity.Brand;
import io.github.douglas.ms_product.model.repository.BrandRepository;
import io.github.douglas.ms_product.service.BrandService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandDTO registerBrand(BrandDTO request) {
        checkName(request.name().toUpperCase());
        return new BrandDTO(
                brandRepository.save(new Brand(request.name().toUpperCase())));
    }

    @Cacheable(value = "brand", key = "#request.id()")
    @Override
    public BrandDTO getBrandById(GenericIdHandler request) {
        return new BrandDTO(
                brandRepository.findById(request.id())
                        .orElseThrow(() -> new ResourceNotFoundException(brandNotFoundMessage(request.id()))));
    }

    @Cacheable(value = "brands")
    @Override
    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(BrandDTO::new)
                .toList();
    }

    @Override
    public BrandDTO updateBrand(BrandDTO request) {
        checkName(request.name());
        var brand = brandRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(brandNotFoundMessage(request.id())));
        brand.setName(request.name());
        return new BrandDTO(
                brandRepository.save(brand));
    }

    @Override
    public void deleteBrand(GenericIdHandler request) {
        var brand = brandRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(brandNotFoundMessage(request.id())));
        if (!brand.getProduct().isEmpty())
            throw new DataIntegrityViolationException(format("Brand %s cannot be deleted as it is referenced by products.", request.id()));

        brandRepository.delete(brand);
    }

    private void checkName(String name) {
        if (brandRepository.existsByName(name))
            throw new DataIntegrityViolationException(format("Brand with name %s already registered.", name));
    }

    private static String brandNotFoundMessage(UUID id) {
        return format("Brand not found with id: %s.", id);
    }
}
