package io.github.douglas.ms_order.dto.order;


import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;

import java.time.LocalDateTime;

public record HistoryDTO(
        Sources source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
}
