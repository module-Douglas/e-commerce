package io.github.douglas.ms_orchestrator.service.impl;

import io.github.douglas.ms_orchestrator.broker.KafkaProducer;
import io.github.douglas.ms_orchestrator.dto.event.Event;
import io.github.douglas.ms_orchestrator.dto.event.History;
import io.github.douglas.ms_orchestrator.enums.Sources;
import io.github.douglas.ms_orchestrator.enums.Status;
import io.github.douglas.ms_orchestrator.enums.Topics;
import io.github.douglas.ms_orchestrator.saga.SagaExecutionController;
import io.github.douglas.ms_orchestrator.service.OrchestratorService;
import io.github.douglas.ms_orchestrator.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static io.github.douglas.ms_orchestrator.enums.Sources.MS_ORCHESTRATOR;
import static io.github.douglas.ms_orchestrator.enums.Status.FAIL;
import static io.github.douglas.ms_orchestrator.enums.Status.SUCCESS;
import static io.github.douglas.ms_orchestrator.enums.Topics.NOTIFY_ENDING;

@Service
public class OrchestratorServiceImpl implements OrchestratorService {


    private static final Logger log = LoggerFactory.getLogger(OrchestratorServiceImpl.class);
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
        log.info("SAGA_STARTED");
        sendToProducerWithTopic(topic, current);
    }

    @Override
    public void finishSagaSuccess(Event event) {
        var current = addHistory(event, "Saga successfully finished", SUCCESS);
        log.info("SAGA FINISHED SUCCESSFULLY FOR EVENT {}", event.id());
        notifyFinishedSaga(current);
    }

    @Override
    public void finishSagaFail(Event event) {
        var current = addHistory(event, "Saga finished with errors", FAIL);
        log.info("SAGA FINISHED WITH ERRORS FOR EVENT {}", event.id());
        notifyFinishedSaga(current);
    }

    @Override
    public void continueSaga(Event event) {
        var topic = getTopic(event);
        log.info("SAGA CONTINUE FOR EVENT {}", event.id());
        sendToProducerWithTopic(topic, event);
    }

    private Event addHistory(Event event, String message, Status status) {
        var history = new History(MS_ORCHESTRATOR, status, message, LocalDateTime.now());
        return event.addHistory(history, MS_ORCHESTRATOR, status);
    }

    private Topics getTopic(Event event) {
        return sagaExecutionController.getNextTopic(event);
    }

    private void notifyFinishedSaga(Event event) {
        kafkaProducer.sendEvent(
                NOTIFY_ENDING, jsonUtil.toJson(event)
        );
    }

    private void sendToProducerWithTopic(Topics topic, Event event) {
        kafkaProducer.sendEvent(
                topic, jsonUtil.toJson(event)
        );
    }
}
