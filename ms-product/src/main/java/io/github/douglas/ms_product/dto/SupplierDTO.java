package io.github.douglas.ms_product.dto;

import io.github.douglas.ms_product.model.entity.Supplier;

import java.time.LocalDateTime;
import java.util.UUID;

public record SupplierDTO(
        UUID id,
        String name,
        String cnpj,
        String email,
        String phoneNumber,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public SupplierDTO(Supplier supplier) {
        this(
                supplier.getId(),
                supplier.getName(),
                supplier.getCnpj(),
                supplier.getEmail(),
                supplier.getPhoneNumber(),
                supplier.getCreatedAt(),
                supplier.getUpdatedAt()
        );
    }
}
