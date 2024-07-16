package io.github.douglas.ms_orchestrator.broker;

import io.github.douglas.ms_orchestrator.service.OrchestratorService;
import io.github.douglas.ms_orchestrator.utils.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

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
        var event = jsonUtil.toEvent(payload);
        orchestratorService.startSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.orchestrator}"
    )
    private void consumeOrchestrator(String payload) {
        var event = jsonUtil.toEvent(payload);
        orchestratorService.continueSaga(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.finish-success}"
    )
    private void consumeFinishSuccess(String payload) {
        var event = jsonUtil.toEvent(payload);
        orchestratorService.finishSagaSuccess(event);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.finish-fail}"
    )
    private void consumeFinishFail(String payload) {
        var event = jsonUtil.toEvent(payload);
        orchestratorService.finishSagaFail(event);
    }

}
