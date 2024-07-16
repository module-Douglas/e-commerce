package io.github.douglas.ms_order.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_order.config.exception.ValidationException;
import io.github.douglas.ms_order.model.entity.Order;
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

    public Order toOrder(String json) {
        try {
            return objectMapper.readValue(json, Order.class);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
