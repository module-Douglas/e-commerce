package io.github.douglas.ms_accounts.broker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {


    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.account-address-validation}"
    )
    private void consumeInventoryValidation(String payload) {

    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.account-address-rollback}"
    )
    private void consumeInventoryRollback(String payload) {

    }
}
