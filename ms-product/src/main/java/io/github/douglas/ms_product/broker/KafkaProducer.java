package io.github.douglas.ms_product.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_product.enums.Topics.ORCHESTRATOR;
import static io.github.douglas.ms_product.enums.Topics.REGISTER_INVENTORY;


@Component
public class KafkaProducer {


    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String payload) {
        try {
            log.info("Sending event to topic {} with data {}", ORCHESTRATOR.getTopic(), payload);
            kafkaTemplate.send(ORCHESTRATOR.getTopic(), payload);
        } catch (Exception e) {
            log.error("Error trying to send data to topic {} with data {}", ORCHESTRATOR.getTopic(), payload, e);
        }
    }

    public void sendInventoryRegister(String payload) {
        try {
            log.info("Sending register inventory event to topic {} with data {}", REGISTER_INVENTORY.getTopic(), payload);
            kafkaTemplate.send(REGISTER_INVENTORY.getTopic(), payload);
        } catch (Exception e) {
            log.error("Error trying to send data to topic {} with data {}", REGISTER_INVENTORY.getTopic(), payload, e);
        }
    }

}
