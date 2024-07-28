package io.github.douglas.ms_order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        UUID productId,
        BigDecimal unitValue,
        Long quantity
) {

}
