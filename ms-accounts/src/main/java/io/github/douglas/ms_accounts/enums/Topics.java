package io.github.douglas.ms_accounts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {

    ORCHESTRATOR("orchestrator"),
    ACCOUNT_ADDRESS_VALIDATION("account-address-validation"),
    ACCOUNT_ADDRESS_ROLLBACK("account-address-rollback");

    private final String topic;

}
