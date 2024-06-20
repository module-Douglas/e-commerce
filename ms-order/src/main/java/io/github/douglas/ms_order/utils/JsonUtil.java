package io.github.douglas.ms_order.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_order.model.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtil {

    private final ObjectMapper objectMapper;

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
            return null;
        }
    }
}
