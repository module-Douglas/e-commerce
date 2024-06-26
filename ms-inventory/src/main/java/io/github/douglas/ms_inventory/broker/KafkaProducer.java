package io.github.douglas.ms_inventory.broker;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_inventory.enums.Topics.ORCHESTRATOR;
import static io.github.douglas.ms_inventory.enums.Topics.SEND_EMAIL;


@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String payload) {
        try {
            kafkaTemplate.send(ORCHESTRATOR.getTopic(), payload);
        } catch(Exception e) {

        }
    }

    public void sendMail(String payload) {
        try {
            kafkaTemplate.send(SEND_EMAIL.getTopic(), payload);
        } catch(Exception e) {

        }
    }
}
