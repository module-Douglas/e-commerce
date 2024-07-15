package io.github.douglas.ms_orchestrator.service.impl;

import io.github.douglas.ms_orchestrator.broker.KafkaProducer;
import io.github.douglas.ms_orchestrator.dto.event.Event;
import io.github.douglas.ms_orchestrator.dto.event.History;
import io.github.douglas.ms_orchestrator.enums.Status;
import io.github.douglas.ms_orchestrator.enums.Topics;
import io.github.douglas.ms_orchestrator.saga.SagaExecutionController;
import io.github.douglas.ms_orchestrator.service.OrchestratorService;
import io.github.douglas.ms_orchestrator.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static io.github.douglas.ms_orchestrator.enums.EventSource.ORCHESTRATOR;
import static io.github.douglas.ms_orchestrator.enums.Status.SUCCESS;

@Service
public class OrchestratorServiceImpl implements OrchestratorService {

    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;
    private final SagaExecutionController sagaExecutionController;


    public OrchestratorServiceImpl(KafkaProducer kafkaProducer, JsonUtil jsonUtil, SagaExecutionController sagaExecutionController) {
        this.kafkaProducer = kafkaProducer;
        this.jsonUtil = jsonUtil;
        this.sagaExecutionController = sagaExecutionController;
    }

    @Override
    public void startSaga(Event event) {
        var current = addHistory(event, "Saga started", SUCCESS);
        var topic = getTopic(current);
        sendToProducerWithTopic(topic, current);
    }

    private Event addHistory(Event event, String message, Status status) {
        var history = new History(ORCHESTRATOR, status, message, LocalDateTime.now());
        return event.addHistory(history);
    }

    private Topics getTopic(Event event) {
        return sagaExecutionController.getNextTopic(event);
    }

    private void sendToProducerWithTopic(Topics topic, Event event) {
        kafkaProducer.sendEvent(
                topic, jsonUtil.toJson(event)
        );
    }
}
