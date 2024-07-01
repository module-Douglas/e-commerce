package io.github.douglas.ms_accounts.broker;

import io.github.douglas.ms_accounts.service.ValidationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final ValidationService validationService;

    public KafkaConsumer(ValidationService validationService) {
        this.validationService = validationService;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.account-address-validation}"
    )
    private void consumeInventoryValidation(String payload) {
        validationService.validateAccountDetails(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.account-address-rollback}"
    )
    private void consumeInventoryRollback(String payload) {
        validationService.realizeRollback(payload);
    }
}
