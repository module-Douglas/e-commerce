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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.douglas.ms_order.enums.Sources.MS_ORDER;
import static io.github.douglas.ms_order.enums.Status.PENDING;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    @Override
    public OrderRequest createOrder(OrderRequest request) {
        var products = request.getProducts().stream()
                .map(Product::of)
                .collect(Collectors.toSet());

        var order = Order.builder()
                .transactionId(String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID()))
                .source(MS_ORDER)
                .status(PENDING)
                .createdAt(LocalDateTime.now())
                .products(products)
                .accountDetails(AccountDetails.builder()
                        .userId(UUID.fromString(request.getAccountId()))
                        .build())
                .deliveryAddress(DeliveryAddress.builder()
                        .addressId(request.getAddressId())
                        .build())
                .build();

        orderRepository.save(order);
        kafkaProducer.sendEvent(
                jsonUtil.toJson(order)
        );

        return request;
    }

}
