package io.github.douglas.ms_accounts.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_accounts.enums.Topics.ORCHESTRATOR;
import static io.github.douglas.ms_accounts.enums.Topics.SEND_EMAIL;

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
        } catch(Exception e) {
            log.error("Error trying to send data to topic {} with data {}", ORCHESTRATOR.getTopic(), payload, e);
        }
    }

    public void sendMail(String payload) {
        try {
            kafkaTemplate.send(SEND_EMAIL.getTopic(), payload);
        } catch(Exception e) {

        }
    }

}
