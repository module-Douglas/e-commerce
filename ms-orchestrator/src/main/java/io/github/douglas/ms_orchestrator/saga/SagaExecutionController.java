package io.github.douglas.ms_orchestrator.saga;

import io.github.douglas.ms_orchestrator.config.exception.ValidationException;
import io.github.douglas.ms_orchestrator.dto.event.Event;
import io.github.douglas.ms_orchestrator.enums.Topics;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static io.github.douglas.ms_orchestrator.saga.SagaHandler.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class SagaExecutionController {

    public Topics getNextTopic(Event event) {
        if (isEmpty(event.source()) || isEmpty(event.status())) {
            throw new ValidationException("Source and Status must be informed");
        }

        return findTopicBySourceAndStatus(event);
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
}
