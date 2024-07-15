package io.github.douglas.ms_orchestrator.saga;

import static io.github.douglas.ms_orchestrator.enums.EventSource.*;
import static io.github.douglas.ms_orchestrator.enums.EventSource.ORCHESTRATOR;
import static io.github.douglas.ms_orchestrator.enums.Status.*;
import static io.github.douglas.ms_orchestrator.enums.Topics.*;

public final class SagaHandler {

    private SagaHandler() {

    }

    public static Object[][] SAGA_HANDLER = {
            {ORCHESTRATOR, SUCCESS, PRODUCT_VALIDATION},
            {ORCHESTRATOR, FAIL, FINISH_FAIL},

            {MS_PRODUCT, ROLLBACK_PENDING, PRODUCT_ROLLBACK},
            {MS_PRODUCT, FAIL, FINISH_FAIL},
            {MS_PRODUCT, SUCCESS, ACCOUNT_ADDRESS_VALIDATION},

            {MS_ACCOUNTS, ROLLBACK_PENDING, ACCOUNT_ADDRESS_ROLLBACK},
            {MS_ACCOUNTS, FAIL, PRODUCT_ROLLBACK},
            {MS_ACCOUNTS, SUCCESS, INVENTORY_VALIDATION},

            {MS_INVENTORY, ROLLBACK_PENDING, INVENTORY_ROLLBACK},
            {MS_INVENTORY, FAIL, ACCOUNT_ADDRESS_ROLLBACK},
            {MS_INVENTORY, SUCCESS, FINISH_SUCCESS}
    };

    public static final Integer EVENT_SOURCE_INDEX = 0;
    public static final Integer SAGA_STATUS_INDEX = 1;
    public static final Integer TOPIC_INDEX = 2;

}
