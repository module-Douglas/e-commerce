package io.github.douglas.ms_order.dto.order;


import io.github.douglas.ms_order.dto.ProductDTO;
import io.github.douglas.ms_order.enums.Status;
import io.github.douglas.ms_order.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderDTO(
        String id,
        String transactionId,
        AccountDetailsDTO accountDetails,
        DeliveryAddressDTO deliveryAddress,
        BigDecimal totalAmount,
        Long totalItems,
        LocalDateTime createdAt,
        Status status,
        Set<ProductDTO> products,
        List<HistoryDTO> historic
) {
    public OrderDTO(Order order) {
        this(
                order.getId(),
                order.getTransactionId(),
                new AccountDetailsDTO(
                        order.getAccountDetails().getUserId(),
                        order.getAccountDetails().getEmail()),
                new DeliveryAddressDTO(
                        order.getDeliveryAddress().getAddressId(),
                        order.getDeliveryAddress().getZipCode(),
                        order.getDeliveryAddress().getStreet(),
                        order.getDeliveryAddress().getNeighborhood(),
                        order.getDeliveryAddress().getComplement(),
                        order.getDeliveryAddress().getNumber(),
                        order.getDeliveryAddress().getCity(),
                        order.getDeliveryAddress().getState()
                ),
                order.getTotalAmount(),
                order.getTotalItems(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getProducts().stream()
                        .map(ProductDTO::new)
                        .collect(Collectors.toSet()),
                order.getHistoric().stream()
                        .map(HistoryDTO::new)
                        .toList()
        );
    }
}
