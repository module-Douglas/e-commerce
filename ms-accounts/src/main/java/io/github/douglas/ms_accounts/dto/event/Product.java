package io.github.douglas.ms_accounts.dto.event;

import java.math.BigDecimal;

public record Product(
        String productId,
        BigDecimal unitValue,
        Long quantity
) {
}
