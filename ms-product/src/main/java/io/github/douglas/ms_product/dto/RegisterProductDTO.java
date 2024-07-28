package io.github.douglas.ms_product.dto;


import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record RegisterProductDTO(
        UUID id,
        String name,
        String description,
        String brand,
        BigDecimal unitValue,
        Long stockAmount,
        String supplierId,
        Set<UUID> categories
) {

}
