package io.github.douglas.ms_order.dto;

import java.util.Set;

public record OrderRequest(
        String accountId,
        String addressId,
        Set<ProductDTO> products
) {


}
