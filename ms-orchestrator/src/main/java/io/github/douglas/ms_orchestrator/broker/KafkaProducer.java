package io.github.douglas.ms_orchestrator.broker;

import io.github.douglas.ms_orchestrator.enums.Topics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Topics topic, String payload) {
        try {
            kafkaTemplate.send(topic.getTopic(), payload);
        } catch(Exception e) {

        }
    }

}
