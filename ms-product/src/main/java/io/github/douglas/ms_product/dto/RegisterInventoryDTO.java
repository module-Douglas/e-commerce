package io.github.douglas.ms_product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterInventoryDTO(
        UUID productId,
        BigDecimal unitValue,
        Long stockAmount
) {

    public RegisterInventoryDTO{

    }
}
