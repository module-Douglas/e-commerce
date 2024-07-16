package io.github.douglas.ms_order.service.impl;

import io.github.douglas.ms_order.broker.KafkaProducer;
import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.model.entity.AccountDetails;
import io.github.douglas.ms_order.model.entity.DeliveryAddress;
import io.github.douglas.ms_order.model.entity.Order;
import io.github.douglas.ms_order.model.entity.Product;
import io.github.douglas.ms_order.model.repository.OrderRepository;
import io.github.douglas.ms_order.service.OrderService;
import io.github.douglas.ms_order.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.douglas.ms_order.enums.Sources.MS_ORDER;
import static io.github.douglas.ms_order.enums.Status.PENDING;

@Service

public class OrderServiceImpl implements OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaProducer kafkaProducer, JsonUtil jsonUtil) {
        this.orderRepository = orderRepository;
        this.kafkaProducer = kafkaProducer;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public OrderRequest createOrder(OrderRequest request) {
        var products = request.products().stream()
                .map(Product::new)
                .collect(Collectors.toSet());

        var order = new Order();
        order.setTransactionId(String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID()));
        order.setProducts(products);
        order.setCreatedAt(LocalDateTime.now());
        order.setSource(MS_ORDER);
        order.setStatus(PENDING);
        order.setCurrentSource(MS_ORDER);
        order.setAccountDetails(new AccountDetails(request.accountId()));
        order.setDeliveryAddress(new DeliveryAddress(request.addressId()));

        orderRepository.save(order);
        kafkaProducer.sendEvent(
                jsonUtil.toJson(order)
        );

        return request;
    }

    @Override
    public void updateOrderStatus(String payload) {
        var order = jsonUtil.toOrder(payload);
        orderRepository.save(order);
    }

}
