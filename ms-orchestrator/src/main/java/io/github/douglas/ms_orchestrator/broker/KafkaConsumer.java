package io.github.douglas.ms_orchestrator.broker;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.start-saga}"
    )
    private void consumeStartSaga(String payload) {

    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.orchestrator}"
    )
    private void consumeOrchestrator(String payload) {

    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.finish-success}"
    )
    private void consumeFinishSuccess(String payload) {

    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.finish-fail}"
    )
    private void consumeFinishFail(String payload) {

    }

}
