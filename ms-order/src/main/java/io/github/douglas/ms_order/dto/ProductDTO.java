package io.github.douglas.ms_order.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;
    private BigDecimal unitValue;
    private Long quantity;

}
