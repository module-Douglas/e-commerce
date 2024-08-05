package io.github.douglas.ms_product.enums;

public enum Topics {

    ORCHESTRATOR("orchestrator"),
    PRODUCT_VALIDATION("product-validation"),
    PRODUCT_ROLLBACK("product-rollback"),
    REGISTER_INVENTORY("register-inventory"),
    UPDATE_INVENTORY("update-inventory");

    private final String topic;

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
