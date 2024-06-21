package io.github.douglas.ms_product.broker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaConsumer {



    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-validation}"
    )
    public void consumeProductValidation(String payload) {

    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-rollback}"
    )
    public void consumerProductRollback(String payload) {

    }


}
