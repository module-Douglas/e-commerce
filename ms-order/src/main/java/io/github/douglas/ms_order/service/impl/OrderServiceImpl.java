package io.github.douglas.ms_order.service.impl;

import io.github.douglas.ms_order.broker.KafkaProducer;
import io.github.douglas.ms_order.config.exception.ResourceNotFoundException;
import io.github.douglas.ms_order.dto.BasicOrderDTO;
import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.dto.order.OrderDTO;
import io.github.douglas.ms_order.model.entity.*;
import io.github.douglas.ms_order.model.repository.OrderRepository;
import io.github.douglas.ms_order.service.OrderService;
import io.github.douglas.ms_order.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.douglas.ms_order.enums.Sources.MS_ORDER;
import static io.github.douglas.ms_order.enums.Status.*;
import static java.lang.String.format;

@Service

public class OrderServiceImpl implements OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaProducer kafkaProducer, JsonUtil jsonUtil) {
        this.orderRepository = orderRepository;
        this.kafkaProducer = kafkaProducer;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public URI createOrder(OrderRequest request) {
        var products = request.products().stream()
                .map(Product::new)
                .collect(Collectors.toSet());

        var order = new Order();
        order.setTransactionId(format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID()));
        order.setProducts(products);
        order.setCreatedAt(LocalDateTime.now());
        order.setSource(MS_ORDER);
        order.setStatus(PENDING);
        order.setCurrentSource(MS_ORDER);
        order.setTotalAmount(calculateTotalAmount(request));
        order.setTotalItems(calculateTotalItems(request));
        order.setAccountDetails(new AccountDetails(request.accountId()));
        order.setDeliveryAddress(new DeliveryAddress(request.addressId()));

        orderRepository.save(order);
        kafkaProducer.sendEvent(
                jsonUtil.toJson(order)
        );

        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(order.getId()).toUri();
    }

    @Override
    public void updateOrderStatus(String payload) {
        var order = jsonUtil.toOrder(payload);
        order.getHistoric().add(
                new History(MS_ORDER, SUCCESS, "Order successfully created.", LocalDateTime.now())
        );
        order.setCurrentSource(MS_ORDER);
        orderRepository.save(order);
        log.info("Order {} with saga notified! TransactionId: {}", order.getId(), order.getTransactionId());
    }

    @Override
    public PageImpl<BasicOrderDTO> getAll(Pageable pageRequest) {
        List<BasicOrderDTO> response = orderRepository.findAll(pageRequest)
                .stream()
                .map(BasicOrderDTO::new)
                .toList();

        return new PageImpl<>(response, pageRequest, response.size());
    }

    @Override
    public OrderDTO getOrderDetails(String id) {
        return new OrderDTO(
                orderRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(format("Order not found with id: %s", id)))
        );
    }

    @Override
    public PageImpl<BasicOrderDTO> getAllOrderByUser(UUID accountId, Pageable pageRequest) {
        List<BasicOrderDTO> response = orderRepository.findAllByAccountDetails_UserId(accountId, pageRequest)
                .stream()
                .map(BasicOrderDTO::new)
                .toList();

        return new PageImpl<>(response, pageRequest, response.size());
    }

    @Override
    public void cancelOrder(String orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Order not found with id: %s.", orderId)));

        order.setStatus(CANCELED);
        orderRepository.save(order);
    }

    private BigDecimal calculateTotalAmount(OrderRequest request) {
        BigDecimal sum = BigDecimal.ZERO;
        for (var product : request.products()) {
            sum = sum.add(product.unitValue().multiply(BigDecimal.valueOf(product.quantity())));
        }
        return sum;
    }

    private Long calculateTotalItems(OrderRequest request) {
        var ref = new Object() {
            Long sum = 0L;
        };
        request.products().forEach(product -> ref.sum += product.quantity());
        return ref.sum;
    }
}
