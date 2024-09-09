package io.github.douglas.ms_order.service;

import io.github.douglas.ms_order.dto.BasicOrderDTO;
import io.github.douglas.ms_order.dto.GenericIdHandler;
import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.dto.order.OrderDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.UUID;

public interface OrderService {
    URI createOrder(OrderRequest request);

    void updateOrderStatus(String payload);

    PageImpl<BasicOrderDTO> getAll(Pageable pageRequest);

    OrderDTO getOrderDetails(String request);

    PageImpl<BasicOrderDTO> getAllOrderByUser(UUID request, Pageable pageRequest);

    void cancelOrder(GenericIdHandler request);
}
