package io.github.douglas.ms_product.broker;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_product.enums.Topics.ORCHESTRATOR;
import static io.github.douglas.ms_product.enums.Topics.REGISTER_INVENTORY;


@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String payload) {
        try {
            kafkaTemplate.send(ORCHESTRATOR.getTopic(), payload);
        } catch (Exception e) {

        }
    }

    public void sendInventoryRegister(String payload) {
        try {
            kafkaTemplate.send(REGISTER_INVENTORY.getTopic(), payload);
        } catch (Exception e) {

        }
    }

}
