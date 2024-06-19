package io.github.douglas.ms_order.model.entity;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {

    private Product product;
    private Long quantity;
}
