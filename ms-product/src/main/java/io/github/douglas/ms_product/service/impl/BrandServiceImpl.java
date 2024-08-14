package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.model.entity.Brand;
import io.github.douglas.ms_product.model.repository.BrandRepository;
import io.github.douglas.ms_product.service.BrandService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
                brandRepository.save(new Brand(request.name().toUpperCase()))
        );
    }

    private void checkName(String name) {
        if (brandRepository.existsByName(name))
            throw new DataIntegrityViolationException(format("Brand with name %s already registered.", name));
    }

}
