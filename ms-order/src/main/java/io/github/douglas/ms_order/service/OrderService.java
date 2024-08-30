package io.github.douglas.ms_order.service;

import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.dto.order.OrderDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.UUID;

public interface OrderService {
    URI createOrder(OrderRequest request);

    void updateOrderStatus(String payload);

    PageImpl<OrderDTO> getAll(Pageable pageRequest);

    OrderDTO getOrderDetails(String id);

    PageImpl<OrderDTO> getAllOrderByUser(UUID accountId, Pageable pageRequest);
}
