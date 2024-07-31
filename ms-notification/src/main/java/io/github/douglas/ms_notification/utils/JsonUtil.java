package io.github.douglas.ms_notification.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.douglas.ms_notification.dto.EmailRequestDTO;
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

    public EmailRequestDTO toObject(String payload) {
        try {
            return objectMapper.readValue(payload, EmailRequestDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

}
