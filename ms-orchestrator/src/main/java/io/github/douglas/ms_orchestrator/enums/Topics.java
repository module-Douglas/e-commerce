package io.github.douglas.ms_orchestrator.enums;


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

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
