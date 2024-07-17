package io.github.douglas.ms_inventory.enums;


public enum Topics {

    ORCHESTRATOR("orchestrator"),
    INVENTORY_VALIDATION("inventory-validation"),
    INVENTORY_ROLLBACK("inventory-rollback"),
    SEND_EMAIL("send-email"),
    LINK_INVENTORY("link-inventory");

    private final String topic;

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
