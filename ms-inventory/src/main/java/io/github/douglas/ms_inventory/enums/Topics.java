package io.github.douglas.ms_inventory.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {

    ORCHESTRATOR("orchestrator"),
    INVENTORY_VALIDATION("inventory-validation"),
    INVENTORY_ROLLBACK("inventory-rollback"),
    SEND_EMAIL("send-email");

    private final String topic;

}
