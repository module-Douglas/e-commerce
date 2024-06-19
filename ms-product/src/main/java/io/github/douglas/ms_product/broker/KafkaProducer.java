package io.github.douglas.ms_product.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_product.enums.Topics.ORCHESTRATOR;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String payload) {
        try {
            log.info("SENDING EVENT TO TOPIC {} WITH DATA {}", ORCHESTRATOR.getTopic(), payload);
            kafkaTemplate.send(ORCHESTRATOR.getTopic(), payload);
        } catch (Exception e) {
            log.error("ERROR TRYING TO SEND DATA TO TOPIC {} WITH DATA {}", ORCHESTRATOR.getTopic(), payload, e);
        }
    }

}
