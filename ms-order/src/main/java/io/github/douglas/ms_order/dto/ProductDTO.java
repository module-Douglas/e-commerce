package io.github.douglas.ms_order.dto;

import java.math.BigDecimal;

public record ProductDTO(
        String productId,
        BigDecimal unitValue,
        Long quantity
) {

}
