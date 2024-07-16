package io.github.douglas.ms_product.dto.event;

import io.github.douglas.ms_product.enums.Sources;
import io.github.douglas.ms_product.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.ObjectUtils.isEmpty;

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
        Set<History> historic
) {
    public Event addHistory(History history, Sources currentSource, Status orderStatus) {
        var newHistoric = new HashSet<History>(this.historic);
        newHistoric.add(history);
        return new Event(id, transactionId, accountDetails, deliveryAddress, totalAmount, totalItems, createdAt, source, currentSource, orderStatus, products, newHistoric);
    }
}
