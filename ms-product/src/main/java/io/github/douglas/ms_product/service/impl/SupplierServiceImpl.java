package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.dto.SupplierDTO;
import io.github.douglas.ms_product.model.entity.Supplier;
import io.github.douglas.ms_product.model.repository.SupplierRepository;
import io.github.douglas.ms_product.service.SupplierService;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierDTO registerSupplier(SupplierDTO request) {
        supplierRepository.save(new Supplier(request));
        return request;
    }

}
