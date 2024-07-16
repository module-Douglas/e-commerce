package io.github.douglas.ms_orchestrator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_orchestrator.config.exception.ValidationException;
import io.github.douglas.ms_orchestrator.dto.event.Event;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public JsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Event toEvent(String payload) {
        try {
            return objectMapper.readValue(payload, Event.class);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
