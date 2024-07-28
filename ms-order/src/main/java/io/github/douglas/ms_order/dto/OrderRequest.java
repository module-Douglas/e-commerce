package io.github.douglas.ms_order.dto;

import java.util.Set;
import java.util.UUID;

public record OrderRequest(
        UUID accountId,
        UUID addressId,
        Set<ProductDTO> products
) {
}
