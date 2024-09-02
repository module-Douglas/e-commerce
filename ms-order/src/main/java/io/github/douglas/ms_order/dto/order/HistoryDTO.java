package io.github.douglas.ms_order.dto.order;


import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;
import io.github.douglas.ms_order.model.entity.History;

import java.time.LocalDateTime;

public record HistoryDTO(
        Sources source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
    public HistoryDTO(History history) {
        this(
                history.getSource(),
                history.getStatus(),
                history.getMessage(),
                history.getCreatedAt()
        );
    }
}
