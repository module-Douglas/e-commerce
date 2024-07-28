package io.github.douglas.ms_orchestrator.broker;

import io.github.douglas.ms_orchestrator.service.OrchestratorService;
import io.github.douglas.ms_orchestrator.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final OrchestratorService orchestratorService;
    private final JsonUtil jsonUtil;

    public KafkaConsumer(OrchestratorService orchestratorService, JsonUtil jsonUtil) {
        this.orchestratorService = orchestratorService;
        this.jsonUtil = jsonUtil;
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.start-saga}"
    )
    private void consumeStartSaga(String payload) {
        log.info("Receiving event {} from start-saga topic", payload);
        var event = jsonUtil.toEvent(payload);
        orchestratorService.startSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.orchestrator}"
    )
    private void consumeOrchestrator(String payload) {
        log.info("Receiving event {} from orchestrator topic", payload);
        var event = jsonUtil.toEvent(payload);
        orchestratorService.continueSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.finish-success}"
    )
    private void consumeFinishSuccess(String payload) {
        log.info("Receiving event {} from finish-success topic", payload);
        var event = jsonUtil.toEvent(payload);
        orchestratorService.finishSagaSuccess(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.finish-fail}"
    )
    private void consumeFinishFail(String payload) {
        log.info("Receiving event {} from finish-fail topic", payload);
        var event = jsonUtil.toEvent(payload);
        orchestratorService.finishSagaFail(event);
    }

}
