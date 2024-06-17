package io.github.douglas.ms_orchestrator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {

    START_SAGA("start-saga"),
    ORCHESTRATOR("orchestrator"),
    FINISH_SUCCESS("finish-success"),
    FINISH_FAIL("finish-fail"),
    NOTIFY_ENDING("notify-ending"),
    PRODUCT_VALIDATION("product-validation"),
    PRODUCT_ROLLBACK("product-rollback"),
    ACCOUNT_ADDRESS_VALIDATION("account-address-validation"),
    ACCOUNT_ADDRESS_ROLLBACK("account-address-rollback"),
    INVENTORY_VALIDATION("inventory-validation"),
    INVENTORY_ROLLBACK("inventory-rollback"),
    SEND_EMAIL("send-email");

    private final String topic;
}
