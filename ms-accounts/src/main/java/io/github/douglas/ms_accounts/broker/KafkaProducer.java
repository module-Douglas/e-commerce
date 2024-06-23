package io.github.douglas.ms_accounts.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_accounts.enums.Topics.ORCHESTRATOR;
import static io.github.douglas.ms_accounts.enums.Topics.SEND_EMAIL;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String payload) {
        try {
            log.info("SENDING TO TOPIC {} WITH PAYLOAD {}", ORCHESTRATOR.getTopic(), payload);
            kafkaTemplate.send(ORCHESTRATOR.getTopic(), payload);
        } catch(Exception e) {
            log.error("ERROR TRYING SEND TO TOPIC {} WITH PAYLOAD {}", ORCHESTRATOR.getTopic(), payload, e);
        }
    }

    public void sendMail(String payload) {
        try {
            log.info("SENDING MAIL REQUEST TO TOPIC {} WITH PAYLOAD {}", SEND_EMAIL.getTopic(), payload);
            kafkaTemplate.send(SEND_EMAIL.getTopic(), payload);
        } catch(Exception e) {
            log.error("ERROR TRYING SEND EMAIL REQUEST TO TOPIC {} WITH PAYLOAD {}", SEND_EMAIL.getTopic(), payload, e);
        }
    }

}