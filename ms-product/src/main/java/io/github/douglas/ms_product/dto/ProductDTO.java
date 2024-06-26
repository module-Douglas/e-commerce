package io.github.douglas.ms_product.dto;

import java.math.BigDecimal;
import java.util.Set;

public record ProductDTO(
        String name,
        String description,
        BigDecimal unitValue,
        Long stockAmount,
        String supplierId,
        Set<Long> categories
) {
}
