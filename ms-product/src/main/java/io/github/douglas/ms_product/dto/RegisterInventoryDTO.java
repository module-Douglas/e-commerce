package io.github.douglas.ms_product.dto;

import java.math.BigDecimal;

public record RegisterInventoryDTO(
        Long productId,
        BigDecimal unitValue,
        Long stockAmount
) {

    public RegisterInventoryDTO{

    }
}
