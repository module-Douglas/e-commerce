package io.github.douglas.ms_product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {

    ORCHESTRATOR("orchestrator"),
    PRODUCT_VALIDATION("product-validation"),
    PRODUCT_ROLLBACK("product-rollback");

    private final String topic;
}
