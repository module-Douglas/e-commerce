package io.github.douglas.ms_accounts.broker;

import io.github.douglas.ms_accounts.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {


    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ValidationService validationService;

    public KafkaConsumer(ValidationService validationService) {
        this.validationService = validationService;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.account-address-validation}"
    )
    private void consumeInventoryValidation(String payload) {
        log.info("Receiving event {} from account-address-validation topic}", payload);
        validationService.validateAccountDetails(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.account-address-rollback}"
    )
    private void consumeInventoryRollback(String payload) {
        log.info("Receiving event {} from account-address-rollback topic}", payload);
        validationService.realizeRollback(payload);
    }
}
