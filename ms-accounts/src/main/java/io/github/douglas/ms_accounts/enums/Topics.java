package io.github.douglas.ms_accounts.enums;

public enum Topics {

    ORCHESTRATOR("orchestrator"),
    ACCOUNT_ADDRESS_VALIDATION("account-address-validation"),
    ACCOUNT_ADDRESS_ROLLBACK("account-address-rollback"),
    SEND_EMAIL("send-email");

    private final String topic;

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
