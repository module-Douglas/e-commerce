package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.dto.ProductDTO;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long productId;
    private BigDecimal unitValue;
    private Long quantity;

    public static Product of(ProductDTO productDTO) {
        return Product.builder()
                .productId(productDTO.getProductId())
                .unitValue(BigDecimal.valueOf(productDTO.getUnitValue().doubleValue()))
                .quantity(productDTO.getQuantity())
                .build();
    }
}
