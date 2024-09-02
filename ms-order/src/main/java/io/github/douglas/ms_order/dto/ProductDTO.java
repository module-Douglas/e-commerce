package io.github.douglas.ms_order.dto;

import io.github.douglas.ms_order.model.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        UUID productId,
        BigDecimal unitValue,
        Long quantity
) {

    public ProductDTO(Product product) {
        this(
                product.getProductId(),
                product.getUnitValue(),
                product.getQuantity()
        );
    }

}
