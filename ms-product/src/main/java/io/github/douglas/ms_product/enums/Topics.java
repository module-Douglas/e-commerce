package io.github.douglas.ms_product.enums;

public enum Topics {

    ORCHESTRATOR("orchestrator"),
    PRODUCT_VALIDATION("product-validation"),
    PRODUCT_ROLLBACK("product-rollback"),
    REGISTER_INVENTORY("register-inventory"),
    UPDATE_INVENTORY("update-inventory"),
    LINK_INVENTORY("link-inventory"),
    UPDATE_PRODUCT_STATUS("update-product-status");

    private final String topic;

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
