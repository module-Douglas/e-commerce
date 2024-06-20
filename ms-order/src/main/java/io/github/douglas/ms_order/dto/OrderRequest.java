package io.github.douglas.ms_order.dto;

import lombok.*;

import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String accountId;
    private Long addressId;
    private Set<ProductDTO> products;

}
