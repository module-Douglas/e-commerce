package io.github.douglas.ms_order.resource;

import io.github.douglas.ms_order.dto.GenericIdHandler;
import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> registerOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(orderService.createOrder(request)).build();
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(Pageable pageRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAll(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id")String request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderDetails(request));
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<?> getOrdersByAccount(@PathVariable("id") UUID request, Pageable pageRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getAllOrderByUser(request, pageRequest));
    }

    @PatchMapping
    public ResponseEntity<?> cancelOrder(@RequestBody GenericIdHandler request) {
        orderService.cancelOrder(request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
