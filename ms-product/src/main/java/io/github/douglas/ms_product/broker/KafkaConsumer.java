package io.github.douglas.ms_product.broker;

import io.github.douglas.ms_product.service.ValidationService;
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
            topics = "${spring.kafka.topic.product-validation}"
    )
    public void consumeProductValidation(String payload) {
        log.info("Receiving event {} from product-validation topic}", payload);
        validationService.validateProducts(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-rollback}"
    )
    public void consumerProductRollback(String payload) {
        log.info("Receiving event {} from product-rollback topic}", payload);
        validationService.realizeRollback(payload);
    }


}
