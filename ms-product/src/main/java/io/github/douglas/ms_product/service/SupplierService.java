package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.SupplierDTO;

import java.util.UUID;

public interface SupplierService {
    SupplierDTO registerSupplier(SupplierDTO request);

    SupplierDTO getSupplierDetails(UUID id);

    SupplierDTO updateSupplier(SupplierDTO request);

    void deleteSupplier(SupplierDTO request);
}
