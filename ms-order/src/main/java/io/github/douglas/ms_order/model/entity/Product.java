package io.github.douglas.ms_order.model.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long productId;
    private BigDecimal unitValue;
}
