package io.github.douglas.ms_notification.broker;


import io.github.douglas.ms_notification.service.EmailService;
import io.github.douglas.ms_notification.utils.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final EmailService emailService;
    private final JsonUtil jsonUtil;

    public KafkaConsumer(EmailService emailService, JsonUtil jsonUtil) {
        this.emailService = emailService;
        this.jsonUtil = jsonUtil;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.send-email}"
    )
    private void consumeSendEmail(String payload) {
        emailService.sendEmail(
                jsonUtil.toObject(payload)
        );
    }

}
