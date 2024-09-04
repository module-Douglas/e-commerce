package io.github.douglas.ms_orchestrator.dto.event;


import io.github.douglas.ms_orchestrator.enums.Sources;
import io.github.douglas.ms_orchestrator.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Event(
        String id,
        String transactionId,
        AccountDetails accountDetails,
        DeliveryAddress deliveryAddress,
        BigDecimal totalAmount,
        Long totalItems,
        LocalDateTime createdAt,
        Sources source,
        Sources currentSource,
        Status status,
        Set<Product> products,
        List<History> historic
) {
    public Event addHistory(History history, Sources currentSource, Status orderStatus) {
        var newHistoric = new ArrayList<>(this.historic);
        newHistoric.add(history);
        return new Event(id, transactionId, accountDetails, deliveryAddress, totalAmount, totalItems, createdAt, source, currentSource, orderStatus, products, newHistoric);
    }
}
