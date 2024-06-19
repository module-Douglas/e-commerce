package io.github.douglas.ms_orchestrator.broker;

import io.github.douglas.ms_orchestrator.enums.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(Topics topic, String payload) {
        try {
            log.info("SENDING TO TOPIC {} WITH PAYLOAD {}", topic.getTopic(), payload);
            kafkaTemplate.send(topic.getTopic(), payload);
        } catch(Exception e) {
            log.error("ERROR TRYING SEND TO TOPIC {} WITH PAYLOAD {}", topic.getTopic(), payload);
        }
    }

}
