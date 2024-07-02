package io.github.douglas.ms_inventory.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_inventory.dto.event.Event;
import io.github.douglas.ms_inventory.model.entity.Inventory;
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

    public Inventory toInventory(String payload) {
        try {
            return objectMapper.readValue(payload, Inventory.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Event toEvent(String payload) {
        try {
            return objectMapper.readValue(payload, Event.class);
        } catch (Exception e) {
            return null;
        }
    }
}
