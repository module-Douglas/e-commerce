package io.github.douglas.ms_orchestrator.saga;

import io.github.douglas.ms_orchestrator.config.exception.ValidationException;
import io.github.douglas.ms_orchestrator.dto.event.Event;
import io.github.douglas.ms_orchestrator.enums.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static io.github.douglas.ms_orchestrator.saga.SagaHandler.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class SagaExecutionController {

    private static final Logger log = LoggerFactory.getLogger(SagaExecutionController.class);
    private static final String SAGA_LOG_ID = "ORDER_ID: %S | TRANSACTION_ID: %s";


    public Topics getNextTopic(Event event) {
        if (isEmpty(event.source()) || isEmpty(event.status())) {
            throw new ValidationException("Source and Status must be informed");
        }

        var topic = findTopicBySourceAndStatus(event);
        logCurrentSaga(event, topic);
        return topic;
    }

    private Topics findTopicBySourceAndStatus(Event event) {
        return (Topics) (Arrays.stream(SAGA_HANDLER))
                .filter(row -> isEventSourceAndStatusValid(event, row))
                .map(i -> i[TOPIC_INDEX])
                .findFirst()
                .orElseThrow(() -> new ValidationException("Topic not found."));
    }

    private boolean isEventSourceAndStatusValid(Event event, Object [] row) {
        var source = row[EVENT_SOURCE_INDEX];
        var status = row[SAGA_STATUS_INDEX];

        return event.currentSource().equals(source) && event.status().equals(status);
    }

    private void logCurrentSaga(Event event, Topics topic) {
        String sagaId = createSagaId(event);
        var source = event.currentSource();
        switch (event.status()) {
            case SUCCESS -> log.info("### CURRENT SAGA: {} | SUCCESS | NEXT TOPIC {} | {}",
                    source, topic, sagaId);
            case ROLLBACK_PENDING -> log.info("### CURRENT SAGA {} | SENDING TO ROLLBACK CURRENT SERVICE | NEXT TOPIC {} | {}",
                    source, topic, sagaId);
            case FAIL -> log.info("### CURRENT SAGA {} | SENDING TO ROLLBACK PREVIOUS SERVICE | NEXT TOPIC {} | {}",
                    source, topic, sagaId);
        }
    }

    private String createSagaId(Event event) {
        return String.format(SAGA_LOG_ID,
                event.id(), event.transactionId());
    }


}
