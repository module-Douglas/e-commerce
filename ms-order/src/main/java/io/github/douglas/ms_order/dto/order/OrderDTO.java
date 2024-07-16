package io.github.douglas.ms_order.dto.order;


import io.github.douglas.ms_order.dto.ProductDTO;
import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record OrderDTO(
        String id,
        String transactionId,
        AccountDetailsDTO accountDetailsDTO,
        DeliveryAddressDTO deliveryAddressDTO,
        BigDecimal totalAmount,
        Long totalItems,
        LocalDateTime createdAt,
        Sources source,
        Sources currentSource,
        Status status,
        Set<ProductDTO> products,
        Set<HistoryDTO> historic
) {
    public OrderDTO addHistory(HistoryDTO history, Sources currentSource, Status orderStatus) {
        var newHistoric = new HashSet<HistoryDTO>(this.historic);
        newHistoric.add(history);
        return new OrderDTO(id, transactionId, accountDetailsDTO, deliveryAddressDTO, totalAmount, totalItems, createdAt, source, currentSource, orderStatus, products, newHistoric);
    }
}
