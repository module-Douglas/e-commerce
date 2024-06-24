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
        Status status,
        Set<Product> products,
        Set<History> historic
) {
    public Event addHistory(History history) {
        var newHistoric = new HashSet<History>();
        if (this.historic == null) {
            newHistoric.add(history);
            return new Event(id, transactionId, accountDetails, deliveryAddress, totalAmount, totalItems, createdAt, source, status, products, newHistoric);
        }
        this.historic.add(history);
        return this;
    }
}
