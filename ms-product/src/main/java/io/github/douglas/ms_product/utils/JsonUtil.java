package io.github.douglas.ms_product.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_product.dto.LinkInventory;
import io.github.douglas.ms_product.dto.event.Event;
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
            return null;
        }
    }

    public LinkInventory toLinkInventory(String payload) {
        try {
            return objectMapper.readValue(payload, LinkInventory.class);
        } catch (Exception e) {
            return null;
        }
    }

}
