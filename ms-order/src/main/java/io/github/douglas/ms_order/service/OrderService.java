package io.github.douglas.ms_order.service;

import io.github.douglas.ms_order.dto.OrderRequest;

public interface OrderService {
    OrderRequest createOrder(OrderRequest request);
}
