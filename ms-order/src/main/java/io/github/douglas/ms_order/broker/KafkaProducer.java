package io.github.douglas.ms_order.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.github.douglas.ms_order.enums.Topics.SEND_EMAIL;
import static io.github.douglas.ms_order.enums.Topics.START_SAGA;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String payload) {
        try {
            log.info("SENDING EVENT TO TOPIC {} WITH PAYLOAD {}", START_SAGA.getTopic(), payload);
            kafkaTemplate.send(START_SAGA.getTopic(), payload);
        } catch (Exception e) {
            log.error("ERROR TRYING TO SEND DATA TO TOPIC {} WITH PAYLOAD {}", START_SAGA.getTopic(), payload, e);
        }
    }

    public void sendMail(String payload) {
        try {
            log.info("SENDING MAIL REQUEST TO TOPIC {} WITH PAYLOAD {}", SEND_EMAIL.getTopic(), payload);
            kafkaTemplate.send(SEND_EMAIL.getTopic(), payload);
        } catch (Exception e) {
            log.error("ERROR TRYING TO SEND MAIL REQUEST TO TOPIC {} WITH PAYLOAD {}", SEND_EMAIL.getTopic(), payload, e);
        }
    }

}
