package io.github.douglas.ms_orchestrator.broker;

import io.github.douglas.ms_orchestrator.config.exception.ValidationException;
import io.github.douglas.ms_orchestrator.enums.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Topics topic, String payload) {
        try {
            log.info("Sending event to topic {} with data {}", topic.getTopic(), payload);
            kafkaTemplate.send(topic.getTopic(), payload);
        } catch(Exception e) {
            log.error("Error trying to send data to topic {} with data {}", topic, payload, e);
            throw new ValidationException(e.getMessage());
        }
    }

}
