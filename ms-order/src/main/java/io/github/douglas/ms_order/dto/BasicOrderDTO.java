package io.github.douglas.ms_order.dto;

import io.github.douglas.ms_order.enums.Status;
import io.github.douglas.ms_order.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BasicOrderDTO(
        String id,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        Status status
) {
    public BasicOrderDTO(Order order) {
        this(
                order.getId(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }
}
