package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Supplier;

public record SupplierDTO(
        String id,
        String name,
        String cnpj,
        String email,
        String phoneNumber
) {
    public SupplierDTO generateDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getId().toString(),
                supplier.getName(),
                supplier.getCnpj(),
                supplier.getEmail(),
                supplier.getPhoneNumber()
        );
    }
}
