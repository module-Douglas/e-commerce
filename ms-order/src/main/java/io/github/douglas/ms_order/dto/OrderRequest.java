package io.github.douglas.ms_order.dto;

import java.util.Set;

public record OrderRequest(
        String accountId,
        Long addressId,
        Set<ProductDTO> products
) {


}
