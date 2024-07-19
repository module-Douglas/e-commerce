package io.github.douglas.ms_notification.broker;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.send-email}"
    )
    private void consumeSendEmail(String payload) {

    }

}
