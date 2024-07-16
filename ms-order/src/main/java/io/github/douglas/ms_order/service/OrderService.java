package io.github.douglas.ms_order.service;

import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.dto.order.OrderDTO;

import java.net.URI;

public interface OrderService {
    URI createOrder(OrderRequest request);

    void updateOrderStatus(String payload);

    OrderDTO getOrderDetails(String id);
}
